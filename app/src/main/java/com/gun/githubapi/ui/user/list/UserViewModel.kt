package com.gun.githubapi.ui.user.list

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import com.gun.githubapi.api.retrofit.RetrofitProvider
import com.gun.githubapi.common.BaseViewModel
import com.gun.githubapi.common.state.LoadingState
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.repository.UserRepository
import com.gun.githubapi.data.repository.UserRepositoryImpl
import com.gun.githubapi.data.source.UserRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemotePagingDataSourceImpl
import com.gun.githubapi.ui.user.list.state.DataState
import com.gun.githubapi.ui.user.list.state.EventState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _dataStateFlow: MutableStateFlow<DataState> = MutableStateFlow(DataState.Nothing)
    val dataStateFlow = _dataStateFlow.asStateFlow()

    private val _eventSharedFlow = MutableSharedFlow<EventState>()
    val eventSharedFlow = _eventSharedFlow.asSharedFlow()

    private var dispatchRetry: (() -> Unit)? = null

    enum class Mode {
        SEARCH,
        MOVE_DETAIL
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userService = RetrofitProvider.getUserService()
                val userDataSource = UserRemoteDataSourceImpl(userService)
                val userPagingDataSource = UserRemotePagingDataSourceImpl(userService)
                val userRepository = UserRepositoryImpl(userDataSource, userPagingDataSource)
                UserViewModel(userRepository)
            }
        }
    }

    fun loadingStateChange(isLoading: Boolean) {
        _loadingStateFlow.value = LoadingState(isLoading)
    }

    fun fetchUserList() {
        dispatchRetry = { fetchUserList() }

        loadingStateChange(true)

        _dataStateFlow.value = DataState.Nothing

        viewModelScope.launch(exceptionHandler) {
            // 조회 완료 후 로딩 상태 변경은 페이징 조회 시 UserSearchActivity 의 addLoadStateListener 에 의해 변경
            val userList = userRepository.fetchUserList().flow.cachedIn(viewModelScope)
            _dataStateFlow.value = DataState.ShowUserList(userList.first())
        }
    }

    fun fetchUser(userName: String, mode: Mode) {
        dispatchRetry = { fetchUser(userName, mode) }

        loadingStateChange(true)

        _dataStateFlow.value = DataState.Nothing

        viewModelScope.launch(exceptionHandler) {
            val user = userRepository.fetchUser(userName)

            if (mode == Mode.MOVE_DETAIL) {
                _eventSharedFlow.emit(EventState.MoveDetailActivity(user))
                loadingStateChange(false)
                return@launch
            } else {
                _dataStateFlow.value = DataState.ShowUser(user)
                loadingStateChange(false)
            }
        }
    }

    fun onClickSearch(inputText: String) {
        if (loadingStateFlow.value == LoadingState(true)) {
            return
        }

        val searchData = inputText.replace(" ", "")

        if (searchData.isEmpty()) {
            fetchUserList()
        } else {
            fetchUser(inputText, Mode.SEARCH)
        }
    }

    fun onClickItem(user: User) {
        if (user.existUserDetail()) {
            viewModelScope.launch {
                _eventSharedFlow.emit(EventState.MoveDetailActivity(user))
            }
            return
        }

        fetchUser(user.login, Mode.MOVE_DETAIL)
    }

    fun retryApiRequest() {
        dispatchRetry?.invoke()
    }
}
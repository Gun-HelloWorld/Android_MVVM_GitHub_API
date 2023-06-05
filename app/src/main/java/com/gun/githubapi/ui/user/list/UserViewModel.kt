package com.gun.githubapi.ui.user.list

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.gun.githubapi.api.retrofit.RetrofitProvider
import com.gun.githubapi.common.BaseViewModel
import com.gun.githubapi.common.ext.getErrorData
import com.gun.githubapi.common.ext.getErrorState
import com.gun.githubapi.data.dto.error.ErrorData
import com.gun.githubapi.data.dto.result.ApiResult
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.repository.UserRepository
import com.gun.githubapi.data.repository.UserRepositoryImpl
import com.gun.githubapi.data.source.UserRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemotePagingDataSourceImpl
import com.gun.githubapi.ui.user.list.state.DataState
import com.gun.githubapi.ui.user.list.state.EventState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _dataStateFlow: MutableStateFlow<DataState> = MutableStateFlow(DataState.Nothing)
    val dataStateFlow = _dataStateFlow.asStateFlow()

    private val _eventSharedFlow = MutableSharedFlow<EventState>()
    val eventSharedFlow = _eventSharedFlow.asSharedFlow()

    private var dispatchRetry: (() -> Unit)? = null

    val loadStateListener: ((CombinedLoadStates) -> Unit) = {
        viewModelScope.launch(Dispatchers.Main) {
            it.getErrorState()?.let { error ->
                dataStateChange(DataState.Clear)
                errorSharedFlowChange(error.getErrorData())
            }

            val isLoading = it.source.refresh is LoadState.Loading
            loadingStateFlowChange(isLoading)
        }
    }

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

    fun dataStateChange(dataState: DataState) {
        _dataStateFlow.value = dataState
    }

    fun fetchUserList() {
        dispatchRetry = { fetchUserList() }

        loadingStateFlowChange(true)

        dataStateChange(DataState.Nothing)

        viewModelScope.launch(exceptionHandler) {
            // 조회 완료 후 로딩 상태 변경은 페이징 조회 시 UserSearchActivity 의 addLoadStateListener 에 의해 변경
            val userListFlow = userRepository.fetchUserList().flow.cachedIn(viewModelScope)
            userListFlow.collectLatest {
                dataStateChange(DataState.ShowUserList(it))
            }
        }
    }

    fun fetchUser(userName: String, mode: Mode) {
        dispatchRetry = { fetchUser(userName, mode) }

        loadingStateFlowChange(true)

        dataStateChange(DataState.Nothing)

        viewModelScope.launch(exceptionHandler) {
            when (val response = userRepository.fetchUser(userName)) {
                is ApiResult.ApiSuccess -> {
                    loadingStateFlowChange(false)
                    if (mode == Mode.MOVE_DETAIL) {
                        _eventSharedFlow.emit(EventState.MoveDetailActivity(response.data))
                    } else {
                        dataStateChange(DataState.ShowUser(response.data))
                    }
                }
                is ApiResult.ApiError -> errorSharedFlowChange(ErrorData(response.code, response.message))
                is ApiResult.ApiException -> errorSharedFlowChange(ErrorData(message = response.e.message))
            }
            loadingStateFlowChange(false)
        }
    }

    fun onClickSearch(inputText: String) {
        if (loadingStateFlow.value.isShow) {
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
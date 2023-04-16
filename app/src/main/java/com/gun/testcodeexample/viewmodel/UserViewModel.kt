package com.gun.testcodeexample.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import com.gun.testcodeexample.api.retrofit.RetrofitProvider
import com.gun.testcodeexample.common.BaseViewModel
import com.gun.testcodeexample.common.state.LoadingState
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.data.repository.UserRepository
import com.gun.testcodeexample.data.repository.UserRepositoryImpl
import com.gun.testcodeexample.data.source.UserRemoteDataSourceImpl
import com.gun.testcodeexample.data.source.UserRemotePagingDataSourceImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Nothing)
    val viewState = _viewState.asStateFlow()

    enum class Mode {
        SEARCH,
        MOVE_DETAIL
    }

    sealed class ViewState {
        object Nothing : ViewState()
        data class ShowUserList(val userList: PagingData<User>) : ViewState()
        data class ShowUser(val user: User, val mode: Mode) : ViewState()
        data class MoveDetailActivity(val user: User): ViewState()
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

    fun fetchUserList() {
        _loadingState.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            val userList = userRepository.fetchUserList()
            _viewState.value = ViewState.ShowUserList(userList.flow.first())
            delay(1000)
            _loadingState.value = LoadingState(false)
        }
    }

    fun fetchUser(userName: String, mode: Mode) {
        _loadingState.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            val user = userRepository.fetchUser(userName)

            if (mode == Mode.MOVE_DETAIL) {
                _viewState.value = ViewState.MoveDetailActivity(user)
                _loadingState.value = LoadingState(false)
                return@launch
            }

            _viewState.value = ViewState.ShowUser(user, mode)
            _loadingState.value = LoadingState(false)
        }
    }

    fun onClickSearch(inputText: String) {
        if (loadingState.value == LoadingState(true)) {
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
            _viewState.value = ViewState.MoveDetailActivity(user)
            return
        }

        fetchUser(user.login, Mode.MOVE_DETAIL)
    }
}
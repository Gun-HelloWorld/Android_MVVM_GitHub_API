package com.gun.testcodeexample.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.testcodeexample.api.retrofit.RetrofitProvider
import com.gun.testcodeexample.common.BaseViewModel
import com.gun.testcodeexample.common.state.LoadingState
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.data.repository.UserRepository
import com.gun.testcodeexample.data.repository.UserRepositoryImpl
import com.gun.testcodeexample.data.source.UserRemoteDataSourceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.ShowUserList(mutableListOf()))
    val viewState = _viewState.asStateFlow()

    enum class Mode {
        SEARCH,
        MOVE_DETAIL
    }

    sealed class ViewState {
        data class ShowUserList(val userList: MutableList<User>) : ViewState()
        data class ShowUser(val user: User, val mode: Mode) : ViewState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userService = RetrofitProvider.getUserService()
                val userDataSource = UserRemoteDataSourceImpl(userService)
                val userRepository = UserRepositoryImpl(userDataSource)
                UserViewModel(userRepository)
            }
        }
    }

    fun fetchUserList() {
        _loadingState.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            val userList = userRepository.fetchUserList()
            _viewState.value = ViewState.ShowUserList(userList)
            _loadingState.value = LoadingState(false)
        }
    }

    fun fetchUser(userName: String, mode: Mode) {
        _loadingState.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            val user = userRepository.fetchUser(userName)
            _viewState.value = ViewState.ShowUser(user, mode)
            _loadingState.value = LoadingState(false)
        }
    }
}
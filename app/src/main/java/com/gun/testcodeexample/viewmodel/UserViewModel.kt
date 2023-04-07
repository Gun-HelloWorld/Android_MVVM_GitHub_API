package com.gun.testcodeexample.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.testcodeexample.api.retrofit.RetrofitProvider
import com.gun.testcodeexample.common.BaseViewModel
import com.gun.testcodeexample.common.Constants.TAG
import com.gun.testcodeexample.data.repository.UserRepository
import com.gun.testcodeexample.data.repository.UserRepositoryImpl
import com.gun.testcodeexample.data.source.UserRemoteDataSourceImpl
import com.gun.testcodeexample.data.dto.user.User
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val viewState = MutableLiveData<ViewState>()

    enum class Mode {
        SEARCH,
        MOVE_DETAIL
    }

    sealed class ViewState {
        data class Loading(val isShow: Boolean) : ViewState()
        data class UserListLoadSuccess(val userList: MutableList<User>) : ViewState()
        data class UserLoadSuccess(val user: User, val mode: Mode) : ViewState()
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
        viewState.value = ViewState.Loading(true)

        viewModelScope.launch(exceptionHandler) {
            val userList = userRepository.fetchUserList()
            Log.d(TAG, "UserViewModel - fetchUserList() - userList : $userList")
            viewState.value = ViewState.UserListLoadSuccess(userList)
            viewState.value = ViewState.Loading(false)
        }
    }

    fun fetchUser(userName: String, mode: Mode) {
        viewState.value = ViewState.Loading(true)

        viewModelScope.launch(exceptionHandler) {
            val user = userRepository.fetchUser(userName)
            Log.d(TAG, "UserViewModel - fetchUser() - user : $user")
            viewState.value = ViewState.UserLoadSuccess(user, mode)
            viewState.value = ViewState.Loading(false)
        }
    }
}
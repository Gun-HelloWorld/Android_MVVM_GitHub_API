package com.gun.testcodeexample.ui.user.list

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
import com.gun.testcodeexample.data.user.User
import kotlinx.coroutines.launch

class UserListViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val viewState = MutableLiveData<ViewState>()

    sealed class ViewState {
        data class Loading(val isShow: Boolean) : ViewState()
        data class UserListLoadSuccess(val userList: MutableList<User>) : ViewState()
        data class UserLoadSuccess(val user: User) : ViewState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userService = RetrofitProvider.getUserService()
                val userDataSource = UserRemoteDataSourceImpl(userService)
                val userRepository = UserRepositoryImpl(userDataSource)
                UserListViewModel(userRepository)
            }
        }
    }

    fun fetchUserList() {
        viewState.value = ViewState.Loading(true)

        viewModelScope.launch(exceptionHandler) {
            val userList = userRepository.fetchUserList()
            Log.d(TAG, "UserListViewModel - fetchUserList() - userList : $userList")
            viewState.value = ViewState.UserListLoadSuccess(userList)
            viewState.value = ViewState.Loading(false)
        }
    }

    fun fetchUser(userName: String) {
        viewState.value = ViewState.Loading(true)

        viewModelScope.launch(exceptionHandler) {
            val user = userRepository.fetchUser(userName)
            Log.d(TAG, "UserListViewModel - fetchUser() - user : $user")
            viewState.value = ViewState.UserLoadSuccess(user)
            viewState.value = ViewState.Loading(false)
        }
    }
}
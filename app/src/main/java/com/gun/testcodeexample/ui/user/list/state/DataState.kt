package com.gun.testcodeexample.ui.user.list.state

import androidx.paging.PagingData
import com.gun.testcodeexample.data.dto.user.User

sealed class DataState {
    object Nothing : DataState()
    data class ShowUserList(val userList: PagingData<User>) : DataState()
    data class ShowUser(val user: User) : DataState()
}
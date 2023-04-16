package com.gun.githubapi.ui.user.list.state

import androidx.paging.PagingData
import com.gun.githubapi.data.dto.user.User

sealed class DataState {
    object Nothing : DataState()
    data class ShowUserList(val userList: PagingData<User>) : DataState()
    data class ShowUser(val user: User) : DataState()
}
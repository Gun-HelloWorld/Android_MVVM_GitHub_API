package com.gun.githubapi.data.repository

import androidx.paging.Pager
import com.gun.githubapi.data.dto.user.User

interface UserRepository {
    suspend fun fetchUserList(): Pager<Int, User>
    suspend fun fetchUser(userName: String): User
    suspend fun fetchFollowerList(userName: String): MutableList<User>
    suspend fun fetchFollowingList(userName: String): MutableList<User>
}
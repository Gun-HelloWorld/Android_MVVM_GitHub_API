package com.gun.githubapi.data.repository

import androidx.paging.Pager
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.dto.result.ApiResult

interface UserRepository {
    suspend fun fetchUserList(): Pager<Int, User>
    suspend fun fetchUser(userName: String): ApiResult<User>
    suspend fun fetchFollowerList(userName: String): ApiResult<List<User>>
    suspend fun fetchFollowingList(userName: String): ApiResult<List<User>>
}
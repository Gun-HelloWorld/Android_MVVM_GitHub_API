package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.dto.result.ApiResult

interface UserRemoteDataSource {
    suspend fun fetchUser(userName: String): ApiResult<User>
    suspend fun fetchFollowerList(userName: String): ApiResult<List<User>>
    suspend fun fetchFollowingList(userName: String): ApiResult<List<User>>
}
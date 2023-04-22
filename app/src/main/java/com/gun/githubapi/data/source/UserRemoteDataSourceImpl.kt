package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.dto.result.ApiResult
import com.gun.githubapi.data.service.UserService
import com.gun.githubapi.data.service.handleApi

class UserRemoteDataSourceImpl(private val userService: UserService) : UserRemoteDataSource {
    override suspend fun fetchUser(userName: String): ApiResult<User> {
        return handleApi { userService.fetchUser(userName) }
    }

    override suspend fun fetchFollowerList(userName: String): ApiResult<List<User>> {
        return handleApi { userService.fetchFollowerList(userName) }
    }

    override suspend fun fetchFollowingList(userName: String): ApiResult<List<User>> {
        return handleApi { userService.fetchFollowing(userName) }
    }
}

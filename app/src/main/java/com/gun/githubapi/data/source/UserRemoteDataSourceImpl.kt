package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.service.UserService

class UserRemoteDataSourceImpl(private val userService: UserService) : UserRemoteDataSource {
    override suspend fun fetchUser(userName: String): User {
        return userService.fetchUser(userName)
    }

    override suspend fun fetchFollowerList(userName: String): MutableList<User> {
        return userService.fetchFollowerList(userName)
    }

    override suspend fun fetchFollowingList(userName: String): MutableList<User> {
        return userService.fetchFollowing(userName)
    }
}

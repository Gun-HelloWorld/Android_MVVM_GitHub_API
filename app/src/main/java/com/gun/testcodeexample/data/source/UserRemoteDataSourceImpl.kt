package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.data.service.UserService

class UserRemoteDataSourceImpl(private val userService: UserService) : UserRemoteDataSource {
    override suspend fun fetchUser(userName: String): User {
        return userService.fetchUser(userName)
    }
}

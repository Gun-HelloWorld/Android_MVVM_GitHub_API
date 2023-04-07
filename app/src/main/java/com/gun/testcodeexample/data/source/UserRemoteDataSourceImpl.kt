package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.service.UserService
import com.gun.testcodeexample.data.dto.user.User

class UserRemoteDataSourceImpl(private val userService: UserService) : UserRemoteDataSource {
    override suspend fun fetchUserList(): MutableList<User> {
//        val mockResponse: Response<Account> = Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
//        throw HttpException(mockResponse)
        return userService.fetchUserList()
    }

    override suspend fun fetchUser(userName: String): User {
        return userService.fetchUser(userName)
    }
}

package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.service.UserService
import com.gun.testcodeexample.data.user.User

class UserRemoteDataSourceImpl(private val userService: UserService) : UserRemoteDataSource {
    override suspend fun getUserList(): MutableList<User> {
//        val mockResponse: Response<Account> = Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
//        throw HttpException(mockResponse)
        return userService.getUserList()
    }

    override suspend fun getUser(userName: String): User {
        return userService.getUser(userName)
    }
}

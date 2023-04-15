package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.dto.user.User

interface UserRemoteDataSource {
    suspend fun fetchUser(userName: String): User
}
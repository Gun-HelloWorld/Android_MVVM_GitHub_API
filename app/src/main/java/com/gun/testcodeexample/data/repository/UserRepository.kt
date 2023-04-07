package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.dto.user.User

interface UserRepository {
    suspend fun fetchUserList(): MutableList<User>
    suspend fun fetchUser(userName: String): User
}
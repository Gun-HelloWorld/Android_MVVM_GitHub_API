package com.gun.testcodeexample.data.service

import com.gun.testcodeexample.data.dto.user.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users")
    suspend fun fetchUserList(): MutableList<User>

    @GET("users/{userName}")
    suspend fun fetchUser(@Path("userName") userName: String): User
}
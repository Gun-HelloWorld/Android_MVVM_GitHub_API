package com.gun.testcodeexample.data.service

import com.gun.testcodeexample.data.user.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users")
    suspend fun getUserList(): MutableList<User>

    @GET("users/{userName}")
    suspend fun getUser(@Path("userName") userName: String): User
}
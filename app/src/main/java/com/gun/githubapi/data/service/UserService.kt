package com.gun.githubapi.data.service

import com.gun.githubapi.data.dto.user.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users")
    suspend fun fetchUserList(@Query("since") since: Int, @Query("per_page") perPage : Int): MutableList<User>

    @GET("users/{userName}")
    suspend fun fetchUser(@Path("userName") userName: String): User
}
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

    @GET("users/{userName}/followers")
    suspend fun fetchFollowerList(@Path("userName") userName: String): MutableList<User>

    @GET("users/{userName}/following")
    suspend fun fetchFollowing(@Path("userName") userName: String): MutableList<User>
}
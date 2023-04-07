package com.gun.testcodeexample.data.service

import com.gun.testcodeexample.data.dto.repository.Repository
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryService {
    @GET("users/{userName}/repos")
    suspend fun getUserRepository(@Path("userName") userName: String): MutableList<Repository>
}
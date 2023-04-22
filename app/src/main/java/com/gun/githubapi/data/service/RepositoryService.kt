package com.gun.githubapi.data.service

import com.gun.githubapi.data.dto.repository.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryService {
    @GET("users/{userName}/repos")
    suspend fun fetchRepositoryList(@Path("userName") userName: String): Response<List<Repository>>
}
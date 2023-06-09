package com.gun.githubapi.api.retrofit

import com.gun.githubapi.api.okhttp.OkHttpProvider
import com.gun.githubapi.data.service.RepositoryService
import com.gun.githubapi.data.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL = "https://api.github.com/"

    private val client = Retrofit
        .Builder()
        .client(OkHttpProvider.getOkHttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUserService(): UserService {
        return client.create(UserService::class.java)
    }

    fun getRepositoryService(): RepositoryService {
        return client.create(RepositoryService::class.java)
    }
}






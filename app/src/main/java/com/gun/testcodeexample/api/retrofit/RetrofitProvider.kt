package com.gun.testcodeexample.api.retrofit

import com.gun.testcodeexample.api.okhttp.OkHttpProvider
import com.gun.testcodeexample.data.service.UserService
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
}






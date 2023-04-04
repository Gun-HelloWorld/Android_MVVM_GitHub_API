package com.gun.testcodeexample.api.okhttp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpProvider {
    fun getOkHttpClient(): OkHttpClient {
        val httpLoginInterceptor = HttpLoggingInterceptor(PrettyLogger()).setLevel(HttpLoggingInterceptor.Level.BODY)
        val requestInterceptor = RequestInterceptor()

        return OkHttpClient.Builder()
            .addInterceptor(httpLoginInterceptor)
            .addInterceptor(requestInterceptor)
            .build()
    }
}
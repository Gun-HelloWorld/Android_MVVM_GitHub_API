package com.gun.testcodeexample.api.okhttp

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

//        builder.addHeader("Auth", "...")

        return chain.proceed(builder.build())
    }
}
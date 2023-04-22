package com.gun.githubapi.data.service

import com.gun.githubapi.data.dto.result.ApiResult
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response


suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            return ApiResult.ApiSuccess(body)
        }

        if (!response.message().isNullOrEmpty()) {
            return ApiResult.ApiError(code = response.code(), message = response.message())
        }

        val errorMessage = response.getErrorBodyMessage("message")

        return ApiResult.ApiError(code = response.code(), message = errorMessage)

    } catch (e: HttpException) {
        ApiResult.ApiError(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        ApiResult.ApiException(e)
    }
}

fun <T> Response<T>?.getErrorBodyMessage(filedName: String): String {
    var errorMessage = "Unknown"

    if (this?.errorBody() == null || filedName.isEmpty()) {
        return errorMessage
    }

    return try {
        val jsonString = errorBody()!!.string()
        val jsonObject = JSONObject(jsonString)

        if (jsonObject.has(filedName)) {
            errorMessage = jsonObject.getString(filedName)
        }

        errorMessage
    } catch (e: JSONException) {
        e.printStackTrace()
        errorMessage
    }
}
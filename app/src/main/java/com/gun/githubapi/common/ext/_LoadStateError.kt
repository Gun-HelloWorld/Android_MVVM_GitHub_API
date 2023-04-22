package com.gun.githubapi.common.ext

import androidx.paging.LoadState
import com.gun.githubapi.data.dto.error.ErrorData
import com.gun.githubapi.data.service.getErrorBodyMessage
import retrofit2.HttpException
import java.io.IOException

fun LoadState.Error?.getErrorData(): ErrorData {
    var errorData = ErrorData(message = "Unknown")

    if (this == null) {
        return errorData
    }

    errorData = when (val throwable = error) {
        is HttpException -> {
            val errorMessage = throwable.response()?.getErrorBodyMessage("message")
            ErrorData(code = throwable.code(), errorMessage)
        }
        is IOException -> {
            ErrorData(message = throwable.message)
        }
        else -> {
            ErrorData(message = throwable.message)
        }
    }

    return errorData
}
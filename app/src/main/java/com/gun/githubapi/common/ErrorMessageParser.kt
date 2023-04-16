package com.gun.githubapi.common

import android.content.res.Resources
import com.gun.githubapi.R
import com.gun.githubapi.common.state.ErrorState
import retrofit2.HttpException

object ErrorMessageParser {
    fun parseToErrorMessage(res: Resources, errorState: ErrorState): String {

        val message = when (errorState) {
            is ErrorState.HttpExceptionState -> {
                val httpException = errorState.throwable as HttpException
                val code = httpException.code()

                if (code == 403) {
                    return res.getString(R.string.msg_error_http_exception_403)
                }

                res.getString(R.string.msg_error_http_exception)
            }
            is ErrorState.IOExceptionState -> res.getString(R.string.msg_error_io_exception)
            is ErrorState.ExceptionState -> res.getString(R.string.msg_error_exception)
        }

        return message
    }
}
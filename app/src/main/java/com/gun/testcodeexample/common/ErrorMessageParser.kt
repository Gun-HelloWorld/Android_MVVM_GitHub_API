package com.gun.testcodeexample.common

import android.content.res.Resources
import com.gun.testcodeexample.R

object ErrorMessageParser {
    fun parseToErrorMessage(res: Resources, errorState: ErrorState): String {
        val message = when(errorState) {
            is ErrorState.HttpExceptionState -> res.getString(R.string.msg_error_http_exception)
            is ErrorState.IOExceptionState -> res.getString(R.string.msg_error_io_exception)
            is ErrorState.ExceptionState -> res.getString(R.string.msg_error_exception)
        }

        return  message
    }
}
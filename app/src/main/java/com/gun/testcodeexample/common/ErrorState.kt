package com.gun.testcodeexample.common

sealed class ErrorState {
    class HttpExceptionState(httpException: retrofit2.HttpException) : ErrorState()
    object IOExceptionState: ErrorState()
    object ExceptionState: ErrorState()
}


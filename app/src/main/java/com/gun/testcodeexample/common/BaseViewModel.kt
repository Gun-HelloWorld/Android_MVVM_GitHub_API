package com.gun.testcodeexample.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    private val _errorState = MutableLiveData<ErrorState>()

    val errorState: LiveData<ErrorState>
        get() = _errorState

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        when (throwable) {
            is HttpException -> _errorState.postValue(ErrorState.HttpExceptionState(throwable))
            is IOException -> _errorState.postValue(ErrorState.IOExceptionState)
            else -> _errorState.postValue(ErrorState.ExceptionState)
        }
    }
}
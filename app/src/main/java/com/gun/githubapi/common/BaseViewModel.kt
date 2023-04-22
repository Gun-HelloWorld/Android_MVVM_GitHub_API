package com.gun.githubapi.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gun.githubapi.common.Constants.TAG
import com.gun.githubapi.common.state.LoadingState
import com.gun.githubapi.data.dto.error.ErrorData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    protected val _errorSharedFlow = MutableSharedFlow<ErrorData>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    protected val _loadingStateFlow = MutableStateFlow(LoadingState(false))
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        Log.e(TAG,"exceptionHandler : throwable : ${throwable.message}")

        viewModelScope.launch {
            _loadingStateFlow.emit(LoadingState(false))

            when (throwable) {
                is HttpException -> _errorSharedFlow.emit(ErrorData(throwable.code(), throwable.message()))
                is IOException -> _errorSharedFlow.emit(ErrorData(message = throwable.message))
                else -> _errorSharedFlow.emit(ErrorData(message = throwable.message))
            }
        }
    }
}
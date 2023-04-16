package com.gun.githubapi.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gun.githubapi.common.Constants.TAG
import com.gun.githubapi.common.state.ErrorState
import com.gun.githubapi.common.state.LoadingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    private val _errorStateFlow = MutableSharedFlow<ErrorState>()
    val errorStateFlow = _errorStateFlow.asSharedFlow()

    protected val _loadingStateFlow = MutableStateFlow(LoadingState(false))
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        Log.e(TAG,"exceptionHandler : throwable : ${throwable.message}")

        viewModelScope.launch {
            _loadingStateFlow.emit(LoadingState(false))

            when (throwable) {
                is HttpException -> _errorStateFlow.emit(ErrorState.HttpExceptionState(throwable))
                is IOException -> _errorStateFlow.emit(ErrorState.IOExceptionState(throwable))
                else -> _errorStateFlow.emit(ErrorState.ExceptionState(throwable))
            }
        }
    }
}
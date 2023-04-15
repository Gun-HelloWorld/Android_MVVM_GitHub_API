package com.gun.testcodeexample.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gun.testcodeexample.common.Constants.TAG
import com.gun.testcodeexample.common.state.ErrorState
import com.gun.testcodeexample.common.state.LoadingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    private val _errorState = MutableSharedFlow<ErrorState>()
    val errorState = _errorState.asSharedFlow()

    protected val _loadingState = MutableStateFlow(LoadingState(false))
    val loadingState = _loadingState.asStateFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        Log.e(TAG,"exceptionHandler : throwable : ${throwable.message}")

        viewModelScope.launch {
            _loadingState.emit(LoadingState(false))

            when (throwable) {
                is HttpException -> _errorState.emit(ErrorState.HttpExceptionState(throwable))
                is IOException -> _errorState.emit(ErrorState.IOExceptionState(throwable))
                else -> _errorState.emit(ErrorState.ExceptionState(throwable))
            }
        }
    }
}
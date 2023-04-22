package com.gun.githubapi.common.ext

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

fun CombinedLoadStates.getErrorState(): LoadState.Error? {
    return when {
        prepend is LoadState.Error -> prepend as LoadState.Error
        append is LoadState.Error -> append as LoadState.Error
        refresh is LoadState.Error -> refresh as LoadState.Error
        else -> null
    }
}
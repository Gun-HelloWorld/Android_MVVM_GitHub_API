package com.gun.githubapi.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gun.githubapi.common.Constants
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.service.UserService

class UserRemotePagingDataSourceImpl(
    private val service: UserService
) : PagingSource<Int, User>() {

    override val keyReuseSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val current = params.key ?: 1

            val response = service.fetchUserList(current, 10)

            LoadResult.Page(
                data = response,
                prevKey = if (current == 1) null else current - 1,
                nextKey = if (response.isEmpty()) null else response.last().id
            )
        } catch (e: Exception) {
            Log.e(Constants.TAG, "ExamplePagingSource - load() - Exception : ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return null
    }
}
package com.gun.testcodeexample.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.data.source.UserRemoteDataSource

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userRemotePagingDataSource: PagingSource<Int, User>
) : UserRepository {

    override suspend fun fetchUserList(): Pager<Int, User> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                userRemotePagingDataSource
            }
        )
    }

    override suspend fun fetchUser(userName: String): User {
        return userRemoteDataSource.fetchUser(userName)
    }
}
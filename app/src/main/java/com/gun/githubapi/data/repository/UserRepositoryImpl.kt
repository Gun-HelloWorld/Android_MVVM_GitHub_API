package com.gun.githubapi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.source.UserRemoteDataSource

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

    override suspend fun fetchUser(userName: String) = userRemoteDataSource.fetchUser(userName)

    override suspend fun fetchFollowerList(userName: String) = userRemoteDataSource.fetchFollowerList(userName)

    override suspend fun fetchFollowingList(userName: String) = userRemoteDataSource.fetchFollowingList(userName)
}
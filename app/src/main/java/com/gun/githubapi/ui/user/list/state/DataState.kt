package com.gun.githubapi.ui.user.list.state

import androidx.paging.PagingData
import com.gun.githubapi.data.dto.user.User

sealed class DataState {
    data class ShowUserList(val userList: PagingData<User>) : DataState()

    data class ShowUser(val user: User) : DataState()

    /** 마지막 상태 필요 시 사용 사용
     *  Ex) DataState 상태가 ShowUser(user) 인 상태에서, 에러 발생 후 다시 재조회 시 동일한 상태를 가지고 있어 Collect 되지 않는 현상
     **/
    object Nothing : DataState()
}
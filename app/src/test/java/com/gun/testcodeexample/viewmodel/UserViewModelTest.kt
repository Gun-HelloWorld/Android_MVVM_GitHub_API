package com.gun.testcodeexample.viewmodel

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.test.MainDispatcherRule
import com.gun.testcodeexample.test.TestDiffCallback
import com.gun.testcodeexample.test.TestListCallback
import com.gun.testcodeexample.ui.user.list.UserViewModel
import com.gun.testcodeexample.ui.user.list.state.DataState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userViewModel = mockk<UserViewModel>(relaxed = true)

    private val testUserList = mutableListOf(
        User(id = 1, login = "User1"),
        User(id = 2, login = "User2"),
        User(id = 3, login = "User3")
    )

    @Test
    fun fetchUserList_success_call() {
        every { userViewModel.fetchUserList() } just Runs

        userViewModel.fetchUserList()

        verify { userViewModel.fetchUserList() }
    }

    @Test
    fun fetchUser_success_call() {
        every { userViewModel.fetchUser(any(), any()) } just Runs

        userViewModel.fetchUser("test", UserViewModel.Mode.SEARCH)

        verify { userViewModel.fetchUser(any(), any()) }
    }

    @Test
    fun fetchUserList_success_dataState_change() = runTest {
        // Given
        val expectedUserData = PagingData.from(testUserList)
        val mockFlow: MutableStateFlow<DataState> = MutableStateFlow(DataState.Nothing)
        val mockViewModel = mockk<UserViewModel>()
        val differ = AsyncPagingDataDiffer<User>( // PagingData 는 값을 추출할 수 없으므로, PagingData 를 RecyclerView 에 매핑하는 헬퍼 클래스 사용
            diffCallback = TestDiffCallback(),
            updateCallback = TestListCallback(),
            workerDispatcher = Dispatchers.Main
        )

        every { mockViewModel.fetchUserList() } just runs
        coEvery { mockViewModel.dataStateFlow } returns mockFlow

        //When
        mockViewModel.fetchUserList()
        mockFlow.value = DataState.ShowUserList(expectedUserData)
        val result = mockViewModel.dataStateFlow.value as DataState.ShowUserList

        val job = launch {
            val pagingData = result.userList
            differ.submitData(pagingData)
        }

        job.join()

        val userList = differ.snapshot().items

        // Then
        assert(mockViewModel.dataStateFlow.value is DataState.ShowUserList)
        assertEquals(testUserList, userList)

        job.cancel()
    }

    @Test
    fun fetchUser_success_dataState_change() {
        // Given
        val expectedData = User("user1",1)
        val mode = UserViewModel.Mode.SEARCH
        val mockFlow: MutableStateFlow<DataState> = MutableStateFlow(DataState.Nothing)
        val mockViewModel = mockk<UserViewModel>()

        //When
        every { mockViewModel.fetchUser("user1", mode) } just runs
        coEvery { mockViewModel.dataStateFlow } returns mockFlow

        mockViewModel.fetchUser("user1", mode)
        mockFlow.value = DataState.ShowUser(expectedData)

        val result = mockViewModel.dataStateFlow.value as DataState.ShowUser

        // Then
        assert(mockViewModel.dataStateFlow.value is DataState.ShowUser)
        assertEquals(expectedData, result.user)
    }
}
package com.gun.githubapi.viewmodel

import androidx.paging.*
import app.cash.turbine.test
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.repository.UserRepository
import com.gun.githubapi.data.repository.UserRepositoryImpl
import com.gun.githubapi.data.service.UserService
import com.gun.githubapi.data.source.UserRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemotePagingDataSourceImpl
import com.gun.githubapi.test.*
import com.gun.githubapi.ui.user.list.UserViewModel
import com.gun.githubapi.ui.user.list.UserViewModel.Mode
import com.gun.githubapi.ui.user.list.state.DataState
import com.gun.githubapi.ui.user.list.state.EventState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@Suppress("NonAsciiCharacters")
@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepository

    private val fakeUserService = mockk<UserService>()

    // PagingData 는 값을 추출할 수 없으므로, PagingData 를 RecyclerView 에 매핑하는 헬퍼 클래스 사용
    private val testPagingDataDiffer = AsyncPagingDataDiffer<User>(
        diffCallback = TestDiffCallback(),
        updateCallback = TestListCallback(),
        workerDispatcher = Dispatchers.Main,
    )

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(UserRemoteDataSourceImpl(fakeUserService), UserRemotePagingDataSourceImpl(fakeUserService))
        userViewModel = UserViewModel(userRepository)

        // PagingData 경우 Paging 에서 자체적으로 에러, 로딩상태 관리하므로
        // 리스너 등록하여 수신된 상태에 따라 뷰모델 errorSharedFlow, loadingStateFlow 를 조작한다.
        testPagingDataDiffer.addLoadStateListener(userViewModel.loadStateListener)
    }

    @Test
    fun `loadingStateChange()_호출_시_loadingStateFlow_변화_테스트`() {
        // [Given]
        val expectedLoadingState = true

        // [When]
        userViewModel.loadingStateFlowChange(expectedLoadingState)
        val actualLoadingState = userViewModel.loadingStateFlow.value.isShow

        // [Then]
        assertEquals(expectedLoadingState, actualLoadingState)
    }

    @Test
    fun `dataStateChange()_호출_시_dataStateFlow_변화_테스트`() = runTest {
        userViewModel.dataStateFlow.test {
            // [Given]
            val testUserList = mutableListOf(
                User(id = 1, login = "User1"),
                User(id = 2, login = "User2"),
                User(id = 3, login = "User3")
            )
            val expectedNothingData = DataState.Nothing
            val expectedShowUserData = DataState.ShowUser(testUserList[0])
            val expectedShowUserListData = DataState.ShowUserList(PagingData.from(testUserList))
            val expectedClearDate = DataState.Clear

            // [When] (Nothing Case)
            userViewModel.dataStateChange(expectedNothingData)
            userViewModel.dataStateChange(expectedShowUserData)
            userViewModel.dataStateChange(expectedShowUserListData)
            userViewModel.dataStateChange(expectedClearDate)

            // [Then]
            assertEquals(expectedNothingData, awaitItem())
            assertEquals(expectedShowUserData, awaitItem())
            assertEquals(expectedShowUserListData, awaitItem())
            assertEquals(expectedClearDate, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchUserList()_호출_시_정상_응답_데이터_확인_테스트`() = runTest {
        // [Given]
        val expectedUserList = mutableListOf(
            User(id = 1, login = "User1"),
            User(id = 2, login = "User2"),
            User(id = 3, login = "User3")
        )

        coEvery { fakeUserService.fetchUserList(any(), any()) } returns expectedUserList

        // [When]
        userViewModel.fetchUserList()

        // [Then]
        userViewModel.dataStateFlow.test {
            submitPagingDataToDiffer((awaitItem() as DataState.ShowUserList).userList)

            val actualUserList = testPagingDataDiffer.snapshot().items
            assertEquals(expectedUserList, actualUserList)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchUserList()_호출_시_에러_발생_후_에러_데이터_확인_테스트`() = runTest {
        userViewModel.errorSharedFlow.test {
            // [Given]
            val errorResult = PagingSource.LoadResult.Error<Int, User>(Exception("에러발생"))
            val expectedErrorMessage = errorResult.throwable.message
            coEvery { fakeUserService.fetchUserList(any(), any()) } throws errorResult.throwable

            // [When]
            userViewModel.fetchUserList()
            doPagingDataConsume()

            // [Then]
            val actualErrorMessage = awaitItem().message // [Step_4] `errorSharedFlow`로 부터 ErrorData 수신
            assertEquals(expectedErrorMessage, actualErrorMessage)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchUser(userName, SEARCH)_호출_시_정상_응답_데이터_확인_테스트`() = runTest {
        // [Given]
        val expectedData = User(id = 1, login = "someUserId", name = "someUserName")
        val mode = Mode.SEARCH
        coEvery { fakeUserService.fetchUser(expectedData.name!!) } returns Response.success(expectedData)

        // [When]
        userViewModel.fetchUser(expectedData.name!!, mode)

        // [Then]
        userViewModel.dataStateFlow.test {
            val actualUser = (awaitItem() as DataState.ShowUser).user
            assertEquals(expectedData, actualUser)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchUser(userName, MOVE_DETAIL)_호출_시_정상_응답_데이터_확인_테스트`() = runTest {
        userViewModel.eventSharedFlow.test {
            // [Given]
            val expectedData = User(id = 1, login = "someUserId", name = "someUserName")
            coEvery { fakeUserService.fetchUser(expectedData.name!!) } returns Response.success(expectedData)

            // [When]
            userViewModel.fetchUser(expectedData.name!!, Mode.MOVE_DETAIL)

            // [Then]
            val actualUser = (awaitItem() as EventState.MoveDetailActivity).user
            assertEquals(expectedData, actualUser)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchUser()_호출_시_에러_발생_후_에러_데이터_확인_테스트`() = runTest {
        val availableModeParameterList = mutableListOf(Mode.SEARCH, Mode.MOVE_DETAIL)

        userViewModel.errorSharedFlow.test {
            // [Given]
            val someUserNameParameter = "someUserName"
            val someModeParameter = availableModeParameterList.random()
            val error = Exception("에러 발생")
            val expectedErrorMessage = error.message
            coEvery { fakeUserService.fetchUser("someUserName")} throws error

            // [When]
            userViewModel.fetchUser(someUserNameParameter, someModeParameter)

            // [Then]
            val actualErrorMessage = awaitItem().message
            assertEquals(expectedErrorMessage, actualErrorMessage)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onClickSearch()_호출_시_이미_로딩_상태인_경우_이후_작업_미실행_테스트`() = runTest {
        // [Given]
        userViewModel.loadingStateFlowChange(true)
        val someUserNameParameter = "someUserName"
        val someSinceParameter = 0
        val somePageParameter = 10

        // [When]
        userViewModel.onClickSearch(someUserNameParameter)

        // [Then]
        coVerify(exactly = 0) { fakeUserService.fetchUserList(someSinceParameter, somePageParameter) }
        coVerify(exactly = 0) { fakeUserService.fetchUser(someUserNameParameter) }
    }

    @Test
    fun `onClickSearch()_호출_시_로딩_상태_아닌경우_파라미터_empty_인_경우_리스트_조회_API_호출_테스트`() = runTest {
        // [Given]
        val emptyUserNameParameter = ""
        coJustRun { fakeUserService.fetchUserList(any(),any())}
        coJustRun { fakeUserService.fetchUser(emptyUserNameParameter)}

        // [When]
        userViewModel.onClickSearch(emptyUserNameParameter)
        doPagingDataConsume()

        // [Then]
        coVerify(exactly = 1) { fakeUserService.fetchUserList(any(), any()) }
        coVerify(exactly = 0) { fakeUserService.fetchUser(emptyUserNameParameter) }
    }

    @Test
    fun `onClickSearch()_호출_시_로딩_상태_아닌경우_파라미터_empty_아닌_경우_단일_조회_API_호출_테스트`() = runTest {
        // [Given]
        val notEmptyUserNameParameter = "someUserName"

        // [When]
        userViewModel.onClickSearch(notEmptyUserNameParameter)

        // [Then]
        coVerify(exactly = 0) { fakeUserService.fetchUserList(any(), any()) }
        coVerify(exactly = 1) { fakeUserService.fetchUser(notEmptyUserNameParameter) }
    }

    @Test
    fun `onClickItem()_호출_시_상세정보_이미_조회한_경우_eventSharedFlow_변화_테스트`() = runTest {
        userViewModel.eventSharedFlow.test {
            // [Given]
            val alreadyLoadedUserDetailData = User(id = 1, login = "someLoginId", name = "fetchUser() 조회하여 수신된 이름")

            // [When]
            userViewModel.onClickItem(alreadyLoadedUserDetailData)

            // [Then]
            val resultUserData = (awaitItem() as EventState.MoveDetailActivity).user
            assertEquals(alreadyLoadedUserDetailData.name, resultUserData.name)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onClickItem()_호출_시_상세정보_아직_조회_안된_경우_eventSharedFlow_변화_테스트`() = runTest {
        userViewModel.eventSharedFlow.test {
            // [Given]
            val notEnoughUserData = User(id = 1, login = "someLoginId", name = null)
            val expectedUserData = User(id = 1, login = "someUserId", name = "someUserName")
            coEvery { fakeUserService.fetchUser(any()) } returns Response.success(expectedUserData)

            // [When]
            userViewModel.onClickItem(notEnoughUserData)

            // [Then]
            val actualUserData = (awaitItem() as EventState.MoveDetailActivity).user
            assertEquals(expectedUserData, actualUserData)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `마지막_호출된_메서드_에러_발생_후_retryApiRequest()_호출_시_재호출_되는지_테스트`() = runTest {
        // [Given] onClickSearch() 호출 시 fetchUserList() 호출 후 에러 발생
        coEvery { fakeUserService.fetchUserList(any(), any()) } throws Exception("에러발생")
        userViewModel.onClickSearch("")
        userViewModel.errorSharedFlow.test {
            doPagingDataConsume()
            awaitItem()
        }

        // [When] retryApiRequest 호출 시 이전 요청 fetchUserList() 재 호출
        coJustRun { fakeUserService.fetchUserList(any(), any()) }
        userViewModel.retryApiRequest()
        doPagingDataConsume()

        // [Then] fakeUserService.fetchUserList() 메서드 두번 호출 됬는지 확인
        coVerify(exactly = 2) { fakeUserService.fetchUserList(any(), any()) }
    }

    /**
     * PagingData 내부에 Cold Stream 인 Flow Consume 처리
     *
     * - 직접적인 dataStateFlow 의 상태를 테스트하는 경우가 아닌, 단순 소비가 필요한 경우 사용되는 메서드.
     *
     * - Cold Stream 은 Consume 하기 전까지 데이터를 발행하지 않으므로 Paging 테스트 시
     *   클라이언트 엔드포인트인 Service(UserService.kt) 까지 정상적으로 호출이 되기 위해선 수신받은 Flow 를 Consume 처리 해주어야 동작
     * */
    private suspend fun doPagingDataConsume() = userViewModel.dataStateFlow.test {
        val showUserListDataState = (expectMostRecentItem() as DataState.ShowUserList)

        submitPagingDataToDiffer(showUserListDataState.userList)

        cancelAndConsumeRemainingEvents()
    }

    /**
     * `PagingData`를 `PagingDataDiffer`에 Submit
     *
     * - PagingDataAdapter(`Paging`용 RecyclerViewAdapter) 에 데이터를 전달하는 것과 같은 역할을 하며
     *   PagingDiffer 로 데이터 전달 시, 성공/실패 데이터에 따라 userViewModel.loadStateListener 로 상태 수신되어 에러, 로딩 상태를 업데이트 한다.
     *
     * - 직접적으로 확인할 수 없는 PagingData 를 `PagingDataDiffer`에 데이터 Submit 후,
     *   PagingDataDiffer.snapshot() 호출을 통해 세부 데이터를 확인 할 수 있다.
     * */
    private fun submitPagingDataToDiffer(pagingData: PagingData<User>) = runTest {
        val job = launch {
            testPagingDataDiffer.submitData(pagingData)
        }

        // 대기열에 남은 항목이 없을 때까지 스케줄러에서 다른 코루틴을 모두 실행
        advanceUntilIdle()

        job.cancel()
    }
}
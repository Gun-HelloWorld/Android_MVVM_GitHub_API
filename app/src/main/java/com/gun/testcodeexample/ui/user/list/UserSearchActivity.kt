package com.gun.testcodeexample.ui.user.list

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.BaseActivity
import com.gun.testcodeexample.common.ErrorMessageParser
import com.gun.testcodeexample.common.recyclerview.ItemClickListener
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.ui.user.detail.UserDetailActivity
import com.gun.testcodeexample.viewmodel.UserViewModel
import com.gun.testcodeexample.viewmodel.UserViewModel.Mode
import com.gun.testcodeexample.viewmodel.UserViewModel.ViewState.*

class UserSearchActivity : BaseActivity(), OnClickListener,
    ItemClickListener<User> {

    private val userViewModel by viewModels<UserViewModel> { UserViewModel.Factory }

    private val rootLayout: ConstraintLayout by lazy { findViewById(R.id.root_layout) }
    private val etSearch: EditText by lazy { findViewById(R.id.et_search) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }

    private val recyclerAdapter = UserSearchRecyclerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        initObserver()
    }

    private fun initLayout() {
        setContentView(R.layout.activity_user_search)

        initLoadingBar(findViewById(R.id.loading_bar))

        recyclerView.adapter = recyclerAdapter

//        val spaceSize = dpToPx(this, 5).toInt()
//        recyclerView.addItemDecoration(CommonItemDecoration(top = spaceSize, bottom = spaceSize))
        findViewById<CardView>(R.id.card_view_search).setOnClickListener(this)
    }

    private fun initObserver() {
        userViewModel.errorState.observe(this) {
            showLoadingBar(false)
            val message = ErrorMessageParser.parseToErrorMessage(resources, it)
            Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT).show()
        }

        userViewModel.viewState.observe(this) {
            when (it) {
                is Loading -> {
                    showLoadingBar(it.isShow)
                }

                is UserListLoadSuccess -> {
                    recyclerAdapter.submitList(it.userList)
                }

                is UserLoadSuccess -> {
                    if (it.mode == Mode.MOVE_DETAIL) {
                        startDetailActivity(it.user)
                        return@observe
                    }

                    recyclerAdapter.submitList(listOf(it.user))
                }
            }
        }
    }

    private fun startDetailActivity(user: User) {
        val pairList = recyclerAdapter.sharedElementsMap[user.login]
        UserDetailActivity.startActivity(this, user, pairList)
    }

    override fun onItemClick(data: User) {
        val isLastLifecycleResume = this.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

        // 중복잡업 막기 (화면 전환 시 애니메이션으로 인해 추가적으로 수명주기 체크 로직 포함)
        if (userViewModel.viewState.value == Loading(true) || !isLastLifecycleResume) {
            return
        }

        if (data.existUserDetail()) {
            startDetailActivity(data)
            return
        }

        userViewModel.fetchUser(data.login, Mode.MOVE_DETAIL)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_view_search -> {
                if (userViewModel.viewState.value == Loading(true)) {
                    return
                }

                val inputText = etSearch.text.toString()
                    .replace(" ", "")

                if (inputText.isEmpty()) {
                    userViewModel.fetchUserList()
                } else {
                    userViewModel.fetchUser(inputText, Mode.SEARCH)
                }
            }
        }
    }
}
package com.gun.testcodeexample.ui.user.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.gun.testcodeexample.R
import com.gun.testcodeexample.api.retrofit.SizeUtils.dpToPx
import com.gun.testcodeexample.common.ErrorMessageParser
import com.gun.testcodeexample.common.recyclerview.CommonItemDecoration
import com.gun.testcodeexample.common.recyclerview.ItemClickListener
import com.gun.testcodeexample.data.user.User
import com.gun.testcodeexample.ui.user.detail.UserDetailActivity
import com.gun.testcodeexample.ui.user.list.UserListViewModel.Mode
import com.gun.testcodeexample.ui.user.list.UserListViewModel.ViewState.*

class UserListActivity : AppCompatActivity(), OnClickListener,
    ItemClickListener<User> {

    private val userListViewModel by viewModels<UserListViewModel> { UserListViewModel.Factory }

    private val rootLayout: ConstraintLayout by lazy { findViewById(R.id.root_layout) }
    private val loadingBar: ContentLoadingProgressBar by lazy { findViewById(R.id.loading_bar) }
    private val etSearch: EditText by lazy { findViewById(R.id.et_search) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }

    private val recyclerAdapter = UserListRecyclerAdapter(this)

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, UserListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        initObserver()
    }

    private fun initLayout() {
        setContentView(R.layout.activity_user_list)

        recyclerView.adapter = recyclerAdapter

        val spaceSize = dpToPx(this, 5).toInt()
        recyclerView.addItemDecoration(CommonItemDecoration(top = spaceSize, bottom = spaceSize))
        findViewById<Button>(R.id.btn_search).setOnClickListener(this)
    }

    private fun initObserver() {
        userListViewModel.errorState.observe(this) {
            showLoadingBar(false)
            val message = ErrorMessageParser.parseToErrorMessage(resources, it)
            Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT).show()
        }

        userListViewModel.viewState.observe(this) {
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

    private fun showLoadingBar(isShow: Boolean) {
        val visibility = if (isShow) View.VISIBLE else View.GONE
        loadingBar.visibility = visibility
    }

    private fun startDetailActivity(user: User) {
        val pairList = recyclerAdapter.sharedElementsMap[user.login]
        UserDetailActivity.startActivity(this, user, pairList)
    }

    override fun onItemClick(data: User) {
        if (data.existUserDetail()) {
            startDetailActivity(data)
            return
        }

        userListViewModel.fetchUser(data.login, Mode.MOVE_DETAIL)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_search -> {
                val inputText = etSearch.text.toString()
                    .replace(" ", "")

                if (inputText.isEmpty()) {
                    userListViewModel.fetchUserList()
                } else {
                    userListViewModel.fetchUser(inputText, Mode.SEARCH)
                }
            }
        }
    }
}
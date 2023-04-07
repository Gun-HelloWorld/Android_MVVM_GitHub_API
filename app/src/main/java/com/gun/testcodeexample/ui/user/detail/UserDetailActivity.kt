package com.gun.testcodeexample.ui.user.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.BaseActivity
import com.gun.testcodeexample.common.Constants.INTENT_KEY_USER_DATA
import com.gun.testcodeexample.common.ErrorMessageParser
import com.gun.testcodeexample.common.ext.loadUserProfile
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.viewmodel.RepositoryViewModel

class UserDetailActivity : BaseActivity() {

    private val layoutRoot: View by lazy { findViewById(R.id.layout_root) }
    private val ivUser: ImageView by lazy { findViewById(R.id.iv_user) }
    private val tvUserNickName: TextView by lazy { findViewById(R.id.tv_user_nickname) }
    private val tvValueRepositoryCnt: TextView by lazy { findViewById(R.id.tv_value_repository_cnt) }
    private val tvValueFollowersCnt: TextView by lazy { findViewById(R.id.tv_value_followers_cnt) }
    private val tvValueFollowingsCnt: TextView by lazy { findViewById(R.id.tv_value_following_cnt) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }

    private val recyclerAdapter = RepositoryRecyclerAdapter()

    private val repositoryViewModel by viewModels<RepositoryViewModel> { RepositoryViewModel.Factory }

    companion object {
        fun startActivity(context: Context, user: User, vararg pairs: Pair<View, String>?) {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_USER_DATA, user)

            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, *pairs)
            context.startActivity(intent, options.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()

        initLayout()
        initObserver()

        initIntentData()?.run {
            showUserData(this)
            repositoryViewModel.fetchRepositoryList(login)
        }
    }

    private fun initLayout() {
        setContentView(R.layout.activity_user_detail)

        initLoadingBar(findViewById(R.id.loading_bar))

        recyclerView.adapter = recyclerAdapter
    }

    private fun initObserver() {
        repositoryViewModel.errorState.observe(this) {
            showLoadingBar(false)
            val message = ErrorMessageParser.parseToErrorMessage(resources, it)
            Snackbar.make(layoutRoot, message, Snackbar.LENGTH_SHORT).show()
        }

        repositoryViewModel.viewState.observe(this) {
            when (it) {
                is RepositoryViewModel.ViewState.Loading -> {
                    showLoadingBar(it.isShow)
                }
                is RepositoryViewModel.ViewState.RepositoryListLoadSuccess -> {
                    recyclerAdapter.submitList(it.repositoryList)
                }
            }
        }
    }

    private fun initIntentData(): User? {
        if (intent == null) {
            return null
        }

        val user: User? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_KEY_USER_DATA, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(INTENT_KEY_USER_DATA) as? User
        }

        return user
    }

    private fun showUserData(user: User) {
        tvUserNickName.text = user.login
        ivUser.loadUserProfile(user.avatarUrl) { supportStartPostponedEnterTransition() }
        tvValueRepositoryCnt.text = user.public_repos.toString()
        tvValueFollowersCnt.text = user.followers.toString()
        tvValueFollowingsCnt.text = user.following.toString()
    }
}
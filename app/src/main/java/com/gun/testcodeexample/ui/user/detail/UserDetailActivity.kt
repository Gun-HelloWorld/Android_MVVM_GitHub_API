package com.gun.testcodeexample.ui.user.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.widget.ContentLoadingProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.Constants.INTENT_KEY_USER_DATA
import com.gun.testcodeexample.common.ErrorMessageParser
import com.gun.testcodeexample.data.user.User

class UserDetailActivity : AppCompatActivity() {

    private val layoutRoot: View by lazy { findViewById(R.id.layout_root) }
    private val loadingBar: ContentLoadingProgressBar by lazy { findViewById(R.id.loading_bar) }
    private val ivUser: ImageView by lazy { findViewById(R.id.iv_user) }
    private val tvUserNickName: TextView by lazy { findViewById(R.id.tv_user_nickname) }
    private val tvValueRepositoryCnt: TextView by lazy { findViewById(R.id.tv_value_repository_cnt) }
    private val tvValueFollowersCnt: TextView by lazy { findViewById(R.id.tv_value_followers_cnt) }
    private val tvValueFollowingsCnt: TextView by lazy { findViewById(R.id.tv_value_following_cnt) }

    private val userDetailViewModel by viewModels<UserDetailViewModel> { UserDetailViewModel.Factory }

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

        initIntentData()
    }

    private fun initLayout() {
        setContentView(R.layout.activity_user_detail)
    }

    private fun initObserver() {
        userDetailViewModel.errorState.observe(this) {
            showLoadingBar(false)
            val message = ErrorMessageParser.parseToErrorMessage(resources, it)
            Snackbar.make(layoutRoot, message, Snackbar.LENGTH_SHORT).show()
        }

        userDetailViewModel.viewState.observe(this) {
            when (it) {
                is UserDetailViewModel.ViewState.Loading -> {
                    showLoadingBar(it.isShow)
                }
                is UserDetailViewModel.ViewState.UserLoadSuccess -> {
                    showData(it.user)
                }
            }
        }
    }

    private fun initIntentData() {
        if (intent == null) {
            return
        }

        val userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_KEY_USER_DATA, User::class.java)
        } else {
            intent.getSerializableExtra(INTENT_KEY_USER_DATA) as? User
        }

        userData?.let {
            tvUserNickName.text = it.login
            ivUser.load(it.avatarUrl) { supportStartPostponedEnterTransition() }

            if (it.name == null) {
                userDetailViewModel.fetchUser(it.login)
                return@let
            }

            showData(it)
        }
    }

    private fun showLoadingBar(isShow: Boolean) {
        val visibility = if (isShow) View.VISIBLE else View.GONE
        loadingBar.visibility = visibility
    }

    private fun showData(user: User) {
        tvValueRepositoryCnt.text = user.public_repos.toString()
        tvValueFollowersCnt.text = user.followers.toString()
        tvValueFollowingsCnt.text = user.following.toString()
    }

    private fun ImageView.load(url: String, onLoadingFinished: () -> Unit = {}) {
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadingFinished()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadingFinished()
                return false
            }
        }
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_profile_blank))
            .listener(listener)
            .into(this)
    }
}
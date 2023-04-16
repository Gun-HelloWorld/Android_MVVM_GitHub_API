package com.gun.testcodeexample.ui.user.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.BaseActivity
import com.gun.testcodeexample.common.Constants.INTENT_KEY_USER_DATA
import com.gun.testcodeexample.common.ErrorMessageParser
import com.gun.testcodeexample.common.ext.loadUserProfile
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.databinding.ActivityUserDetailBinding
import kotlinx.coroutines.launch

class UserDetailActivity : BaseActivity() {
    private val recyclerAdapter = RepositoryRecyclerAdapter()

    private val repositoryViewModel by viewModels<RepositoryViewModel> { RepositoryViewModel.Factory }

    private lateinit var binding: ActivityUserDetailBinding

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
            showUserProfileTransition(this)
            binding.user = this
            repositoryViewModel.fetchRepositoryList(login)
        }
    }

    private fun initLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)

        initLoadingBar(findViewById(R.id.loading_bar))

        binding.recyclerView.adapter = recyclerAdapter
    }

    private fun initObserver() {
        lifecycleScope.launchWhenStarted {

            launch {
                repositoryViewModel.loadingStateFlow.collect {
                    showLoadingBar(it.isShow)
                }
            }
            launch {
                repositoryViewModel.errorStateFlow.collect {
                    val message = ErrorMessageParser.parseToErrorMessage(resources, it)
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                }
            }

            launch {
                repositoryViewModel.dataStateFlow.collect {
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

    private fun showUserProfileTransition(user: User) {
        binding.ivUser.loadUserProfile(user.avatarUrl) { supportStartPostponedEnterTransition() }
    }
}
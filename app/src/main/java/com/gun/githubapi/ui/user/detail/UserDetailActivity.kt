package com.gun.githubapi.ui.user.detail

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.gun.githubapi.R
import com.gun.githubapi.common.BaseActivity
import com.gun.githubapi.common.Constants.INTENT_KEY_USER_DATA
import com.gun.githubapi.common.ErrorMessageParser
import com.gun.githubapi.common.ZoomOutPageTransformer
import com.gun.githubapi.common.ext.loadUserProfile
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.databinding.ActivityUserDetailBinding
import com.gun.githubapi.ui.user.detail.fragment.DetailPagerAdapter
import kotlinx.coroutines.launch

class UserDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    private lateinit var viewPagerAdapter: DetailPagerAdapter

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

        initIntentData()?.run {
            showUserProfileTransition(this)
            userDetailViewModel.setUserNickName(login)
            binding.user = this
            viewPagerAdapter = DetailPagerAdapter(this@UserDetailActivity, getTabTitle(this))
            binding.viewPager.adapter = viewPagerAdapter
            binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
            TabLayoutMediator(binding.tabLayout, binding.viewPager, true, true, viewPagerAdapter).attach()
        }
    }

    private fun getTabTitle(user: User) = arrayOf(
        String.format(getString(R.string.title_tab_repository), user.public_repos),
        String.format(getString(R.string.title_tab_followers), user.followers),
        String.format(getString(R.string.title_tab_following), user.following)
    )

    private fun initLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)

        with(binding) {
            initLoadingBar(loadingBar)
            lifecycleOwner = this@UserDetailActivity
            viewModel = userDetailViewModel
        }

        binding.ivUser.setOnClickListener {
            if (binding.layoutRoot.currentState == R.id.motion_end) {
                binding.layoutRoot.transitionToStart()
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userDetailViewModel.errorStateFlow.collect {
                        val message = ErrorMessageParser.parseToErrorMessage(resources, it)
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                    }
                }

                launch {
                    userDetailViewModel.loadingStateFlow.collect {
                        showLoadingBar(it.isShow)
                    }
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
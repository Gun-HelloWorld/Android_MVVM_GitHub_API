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
import androidx.viewpager2.widget.ViewPager2
import com.gun.githubapi.R
import com.gun.githubapi.common.BaseActivity
import com.gun.githubapi.common.Constants.INTENT_KEY_USER_DATA
import com.gun.githubapi.common.ZoomOutPageTransformer
import com.gun.githubapi.common.ext.loadUserProfile
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.databinding.ActivityUserDetailBinding
import com.gun.githubapi.ui.user.detail.fragment.DetailPagerAdapter
import kotlinx.coroutines.launch

class UserDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewPagerAdapter = DetailPagerAdapter(this)

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
            binding.user = this
            userDetailViewModel.setUserNickName(login)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    private fun initLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)

        with(binding) {
            initLoadingBar(loadingBar)
            lifecycleOwner = this@UserDetailActivity
            viewModel = userDetailViewModel
            viewPager.apply {
                setPageTransformer(ZoomOutPageTransformer())
                registerOnPageChangeCallback(pageChangeCallback)
                adapter = viewPagerAdapter
            }
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
                    userDetailViewModel.selectedPageStateFlow.collect {
                        binding.viewPager.setCurrentItem(it, true)
                        binding.layoutUserDetailCount.layoutRepository.isSelected = it == 0
                        binding.layoutUserDetailCount.layoutFollowers.isSelected = it == 1
                        binding.layoutUserDetailCount.layoutFollowing.isSelected = it == 2
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

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            userDetailViewModel.setSelectedPageStateFlow(position)
        }
    }
}
package com.gun.githubapi.ui.user.detail.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val tabTitleArray: Array<String>
) : FragmentStateAdapter(fragmentActivity), TabLayoutMediator.TabConfigurationStrategy  {

    private val fragmentList = mutableListOf(
        RepositoryFragment.newInstance(),
        FollowersFragment.newInstance(),
        FollowingFragment.newInstance()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.text = tabTitleArray[position]
    }
}
package com.gun.testcodeexample.ui.user.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.Glide
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.recyclerview.BaseListAdapter
import com.gun.testcodeexample.common.recyclerview.BaseViewHolder
import com.gun.testcodeexample.common.recyclerview.ItemClickListener
import com.gun.testcodeexample.data.user.User

class UserListRecyclerAdapter(val listener: ItemClickListener<User>)
    : BaseListAdapter<User, UserListRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_holder, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = currentList[holder.adapterPosition]
        holder.setData(data)
        holder.setClickListener(data)
    }

    inner class UserViewHolder(view: View) : BaseViewHolder(view) {
        private val rootLayout: View by lazy { view.findViewById(R.id.layout_root) }
        private val tvUserName: TextView by lazy { view.findViewById(R.id.tv_user_name) }
        private val tvUserPage: TextView by lazy { view.findViewById(R.id.tv_user_page) }
        private val ivUser: ImageView by lazy { view.findViewById(R.id.iv_user) }

        fun setData(user: User) {
            tvUserName.text = user.login
            tvUserPage.text = user.htmlUrl

            Glide.with(ivUser)
                .load(user.avatarUrl)
                .error(getDrawable(ivUser.context, R.drawable.icon_profile_blank))
                .circleCrop()
                .skipMemoryCache(true)
                .into(ivUser)
        }

        fun setClickListener(user: User) {
            rootLayout.setOnClickListener {
                listener.onItemClick(user)
            }
        }
    }
}
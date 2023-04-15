package com.gun.testcodeexample.ui.user.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.recyclerview.BaseItemCallback
import com.gun.testcodeexample.common.recyclerview.BaseViewHolder
import com.gun.testcodeexample.common.recyclerview.ItemClickListener
import com.gun.testcodeexample.data.dto.user.User

class UserSearchRecyclerAdapter(val listener: ItemClickListener<User>) :
    PagingDataAdapter<User, UserSearchRecyclerAdapter.UserViewHolder>(BaseItemCallback()) {

    var sharedElementsMap: MutableMap<String, Pair<View, String>> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_user_search, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(holder.bindingAdapterPosition)?.let {
            holder.setData(it)
            holder.setClickListener(it)
        }
    }

    inner class UserViewHolder(private val view: View) : BaseViewHolder(view) {
        private val cardView: CardView by lazy { view.findViewById(R.id.card_view) }
        private val tvUserNickname: TextView by lazy { view.findViewById(R.id.tv_user_nickname) }
        private val tvUserPage: TextView by lazy { view.findViewById(R.id.tv_user_page) }
        private val ivUser: ImageView by lazy { view.findViewById(R.id.iv_user) }

        fun setData(user: User) {
            tvUserNickname.text = user.login
            tvUserPage.text = user.htmlUrl

            Glide.with(ivUser)
                .load(user.avatarUrl)
                .error(getDrawable(ivUser.context, R.drawable.icon_profile_blank))
                .circleCrop()
                .skipMemoryCache(true)
                .into(ivUser)
        }

        fun setClickListener(user: User) {
            cardView.setOnClickListener {
                val imageTransitionPair = Pair(ivUser as View, ivUser.transitionName)
                sharedElementsMap[user.login] = imageTransitionPair

                listener.onItemClick(user)
            }
        }
    }
}
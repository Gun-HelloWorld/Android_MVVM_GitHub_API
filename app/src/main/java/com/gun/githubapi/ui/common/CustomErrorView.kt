package com.gun.githubapi.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gun.githubapi.common.ErrorMessageParser
import com.gun.githubapi.common.state.ErrorState
import com.gun.githubapi.databinding.LayoutCustomErrorViewBinding

class CustomErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutCustomErrorViewBinding =
        LayoutCustomErrorViewBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
    }

    fun setRetryClickListener(clickListener: OnClickListener) {
        binding.btnRetry.setOnClickListener(clickListener)
    }

    fun show(errorState: ErrorState) {
        val errorMessage = ErrorMessageParser.parseToErrorMessage(resources, errorState)
        binding.tvError.text = errorMessage
        binding.root.visibility = View.VISIBLE
    }

    fun hide() {
        binding.root.visibility = View.GONE
    }
}
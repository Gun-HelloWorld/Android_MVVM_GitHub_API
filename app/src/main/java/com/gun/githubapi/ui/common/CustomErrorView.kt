package com.gun.githubapi.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.gun.githubapi.R
import com.gun.githubapi.data.dto.error.ErrorData
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

    fun show(errorData: ErrorData) {
        val errorText = if (errorData.code != null) {
            errorData.code.toString() + " : " + errorData.message
        } else {
            errorData.message
        }

        binding.tvError.text = binding.root.context.getString(R.string.msg_error_exception)
        binding.tvErrorDetail.text = errorText
        binding.root.visibility = View.VISIBLE
    }

    fun hide() {
        binding.root.visibility = View.GONE
    }
}
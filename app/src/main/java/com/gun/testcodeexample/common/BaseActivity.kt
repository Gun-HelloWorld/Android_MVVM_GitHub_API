package com.gun.testcodeexample.common

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    private var loadingBar: CustomLoadingBar? = null

    fun initLoadingBar(loadingBar: CustomLoadingBar) {
        this.loadingBar = loadingBar
    }

    fun showLoadingBar(isShow: Boolean) {
        loadingBar?.let {
            it.showLoadingBar(isShow)

            if (isShow) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }
}
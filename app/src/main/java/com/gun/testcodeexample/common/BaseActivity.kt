package com.gun.testcodeexample.common

import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.gun.testcodeexample.api.retrofit.KeyboardUtils

abstract class BaseActivity : AppCompatActivity() {
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

    private fun hideKeyboard() {
        currentFocus?.let {
            val inputManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val iBinder = it.windowToken
            val hideType = InputMethodManager.HIDE_NOT_ALWAYS

            inputManager.hideSoftInputFromWindow(iBinder, hideType)
            it.clearFocus()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (KeyboardUtils.isTouchEditTextOutSide(currentFocus, ev.x.toInt(), ev.y.toInt())) {
            hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }
}
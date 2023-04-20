package com.gun.githubapi.common

import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.gun.githubapi.api.retrofit.KeyboardUtils

abstract class BaseActivity : AppCompatActivity() {

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
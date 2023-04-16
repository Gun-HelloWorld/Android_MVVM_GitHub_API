package com.gun.githubapi.api.retrofit

import android.graphics.Rect
import android.view.View

object KeyboardUtils {
    fun isTouchEditTextOutSide(focusView: View?, touchX: Int, touchY: Int): Boolean {
        if (focusView == null) return false

        val rect = Rect()
        focusView.getGlobalVisibleRect(rect)

        return !rect.contains(touchX, touchY)
    }
}
package com.gun.testcodeexample.api.retrofit

import android.content.Context
import android.util.TypedValue
import androidx.annotation.Dimension

object SizeUtils {
    fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp: Int): Float {
        val r = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        )
    }
}
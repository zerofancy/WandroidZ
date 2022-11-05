package top.ntutn.wandroidz.util

import android.content.res.Resources

val Int.dp: Int
    get() {
        val dpValue = this
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
    }

val Float.dpFloat: Float
    get() {
        val dpValue = this
        return dpValue * Resources.getSystem().displayMetrics.density
    }

val Int.sp: Int
    get() {
        val dpValue = this
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }

val Float.spFloat: Float
    get() {
        val dpValue = this
        return dpValue * Resources.getSystem().displayMetrics.scaledDensity
    }

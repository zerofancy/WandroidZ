package top.ntutn.wandroidz.util

import java.text.SimpleDateFormat

object TimeUtil {
    private val normalSDF = SimpleDateFormat.getDateInstance()

    fun getUserFriendlyTime(time: Long): String {
        val mills = System.currentTimeMillis() - time
        val days = mills.toFloat() / 1000 / 60 / 60/ 24
        if (days < 1) {
            return "今天"
        }
        if (days < 30) {
            return days.toInt().toString() + "天前"
        }
        return normalSDF.format(time)
    }
}
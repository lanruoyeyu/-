package com.fridgekeeper.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000L

    /**
     * 计算保质期剩余天数 = (过期时间 - 当前时间) / 一天毫秒数，向下取整
     */
    fun calculateRemainingDays(expirationDate: Long): Int {
        val now = System.currentTimeMillis()
        val diff = expirationDate - now
        return (diff / MILLIS_PER_DAY).toInt()
    }

    /**
     * 是否过期：remainingDays <= 0
     */
    fun isExpired(remainingDays: Int): Boolean = remainingDays <= 0

    /**
     * 是否警告：0 < remainingDays <= 3
     */
    fun isWarning(remainingDays: Int): Boolean = remainingDays in 1..3

    /**
     * 格式化时间戳为 yyyy-MM-dd 字符串
     */
    fun formatDate(timestamp: Long): String = dateFormat.format(Date(timestamp))

    /**
     * 计算持有天数 = (结束时间 - 起始时间) / 一天毫秒数，向下取整，最少按 1 天
     */
    fun calculateDaysHeld(fromTimestamp: Long, toTimestamp: Long): Int {
        val diff = toTimestamp - fromTimestamp
        return maxOf(1L, diff / MILLIS_PER_DAY).toInt()
    }

    /**
     * 获取今天 0 点的时间戳
     */
    fun todayStartMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

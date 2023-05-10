package com.crystallake.scan.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    // 将date转化成 "yyyyMMdd_HHmmss" 格式：20121003_234131
    fun formatDate2(time: Date?): String {
        val defaultLocale = Locale.CHINA
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", defaultLocale)
        return dateFormat.format(time ?: Date()) ?: System.currentTimeMillis().toString()
    }
}
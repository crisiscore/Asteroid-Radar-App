package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    const val DATE_FORMAT = "yyyy-MM-dd"
}

fun getCurrentDate(): String {
    try {
        val simpleDateFormat = SimpleDateFormat(DateFormat.DATE_FORMAT, Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        return simpleDateFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}
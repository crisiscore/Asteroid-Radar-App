package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = "zzEGceoGW5k6aKL7GoqquNK9VmbmZpxB9sLFd0W3"

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd" , Locale.US)
        val date = Date()
        return formatter.format(date)
    }
}
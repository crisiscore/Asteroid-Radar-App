package com.udacity.asteroidradar

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "picture_of_the_day_table")
data class PictureOfDay(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    @Json(name = "media_type") val mediaType: String?, val title: String,
    val url: String
)
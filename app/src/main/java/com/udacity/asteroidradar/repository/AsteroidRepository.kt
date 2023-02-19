package com.udacity.asteroidradar.repository

import android.util.Log
import com.squareup.moshi.Moshi
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val savedAsteroidList: Flow<List<Asteroid>>
        get() = database.asteroidDao.getAllAsteroids()

    val todayAsteroidList: Flow<List<Asteroid>>
        get() = database.asteroidDao.getAsteroidsByDate(
            startDate = getCurrentDate(),
            endDate = getCurrentDate()
        )

    val weekAsteroidList: Flow<List<Asteroid>>
        get() = database.asteroidDao.getAsteroidsByDate(
            startDate = getCurrentDate(),
            endDate = getEndDate()
        )

    val pictureOfDay: Flow<PictureOfDay>
        get() = database.pictureOfTheDayDao.get()

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroid = Network.retrofitService.getAsteroids(startDate = getCurrentDate() , endDate =  getEndDate(), apiKey = Constants.API_KEY)
                val data = parseAsteroidsJsonResult(JSONObject(asteroid))
                database.asteroidDao.insertAll(data)
            } catch (e: Exception) {
                Log.e("data", e.message.toString())
            }
        }
    }

    suspend fun getPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val response = Network.retrofitService.getPictureOfDay(Constants.API_KEY)
                val image = Moshi.Builder()
                    .build()
                    .adapter(PictureOfDay::class.java)
                    .fromJson(response)
                    ?: PictureOfDay(-1, "", "image", "")

                database.pictureOfTheDayDao.insert(image)
            } catch (e: Exception) {
                Log.e("repository", e.message.toString())
            }
        }
    }

}
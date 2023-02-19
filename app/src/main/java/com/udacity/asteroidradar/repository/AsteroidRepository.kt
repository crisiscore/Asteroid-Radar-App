package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids = database.asteroidDao.getAllAsteroids()

    val todayAsteroidList: LiveData<List<Asteroid>>
        get() = database.asteroidDao.getTodayAsteroid(Constants.getCurrentDate())

    val pictureOfDay: LiveData<PictureOfDay>
        get() = database.pictureOfTheDayDao.get()

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO){
            try {
                val asteroid = Network.retrofitService.getAsteroids(Constants.API_KEY)
                val json = JSONObject(asteroid)
                val data = parseAsteroidsJsonResult(json)
                database.asteroidDao.insertAll(data)
            } catch (e: Exception) {
                Log.e("data", e.message.toString()  )
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
                    ?:
                    PictureOfDay(-1 , "", "image", "")

                database.pictureOfTheDayDao.updateData(image)
            } catch (e: Exception) {
                Log.e("repository", e.message.toString()  )
            }
        }
    }

}
package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList : LiveData<List<Asteroid>>
    get() = _asteroidList

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
    get() = _pictureOfDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()

    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    init {
        onViewWeekAsteroidsClicked()
        try {
            viewModelScope.launch {
                repository.refreshAsteroids()
                repository.getPictureOfTheDay()
                repository.pictureOfDay.collect {
                    _pictureOfDay.value = it
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onTodayAsteroidsClicked() {
        try {
            viewModelScope.launch {
                repository.todayAsteroidList.collect {
                    _asteroidList.value = it
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onViewWeekAsteroidsClicked() {
        try {
            viewModelScope.launch {
                repository.weekAsteroidList.collect {
                    _asteroidList.value = it
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onSavedAsteroidsClicked() {
        try {
            viewModelScope.launch {
                repository.savedAsteroidList.collect {
                    _asteroidList.value = it
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToAsteroidDetails.value = null
    }

}
package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val allAsteroidList = repository.asteroids
    private val todayAsteroidList = repository.todayAsteroidList

    private val _asteroidList = MediatorLiveData<List<Asteroid>>()

    val asteroidList: MutableLiveData<List<Asteroid>>
        get() = _asteroidList

    val pictureOfDay = repository.pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.getPictureOfTheDay()
            _asteroidList.addSource(allAsteroidList) {
                _asteroidList.value = it
            }
        }
    }

    fun onTodayAsteroidsClicked() {
        removeSource()
        _asteroidList.addSource(todayAsteroidList) {
            _asteroidList.value = it
        }
    }

    fun onViewWeekAsteroidsClicked() {
        removeSource()
        _asteroidList.addSource(allAsteroidList) {
            _asteroidList.value = it
        }
    }

    fun onSavedAsteroidsClicked() {
        removeSource()
        _asteroidList.addSource(allAsteroidList) {
            _asteroidList.value = it
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }


    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    private fun removeSource() {
        _asteroidList.removeSource(allAsteroidList)
        _asteroidList.removeSource(todayAsteroidList)
    }

}
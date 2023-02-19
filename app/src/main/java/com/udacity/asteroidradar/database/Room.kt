package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid_table ORDER BY date(closeApproachDate) ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY date(closeApproachDate) ASC ")
    fun getAsteroidsByDate(startDate: String , endDate : String): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<Asteroid>)
}

@Dao
interface PictureOfTheDayDao {
    @Query("SELECT * FROM picture_of_the_day_table")
    fun get(): Flow<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: PictureOfDay)
}

@Database(entities = [Asteroid::class , PictureOfDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfTheDayDao: PictureOfTheDayDao

    companion object{
        private lateinit var INSTANCE: AsteroidDatabase

        fun getDatabase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroids-db").build()
                }
            }
            return INSTANCE
        }
    }

}

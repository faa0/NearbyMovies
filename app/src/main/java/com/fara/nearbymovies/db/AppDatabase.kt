package com.fara.nearbymovies.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fara.nearbymovies.db.dao.CinemaDao
import com.fara.nearbymovies.db.dao.MovieDao
import com.fara.nearbymovies.db.model.Movie
import com.fara.nearbymovies.entity.Cinema

@Database(entities = [Cinema::class, Movie::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cinemaDao(): CinemaDao
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cinema_db.db",
            ).allowMainThreadQueries().build()
    }
}
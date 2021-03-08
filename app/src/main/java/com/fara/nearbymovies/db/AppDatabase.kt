package com.fara.nearbymovies.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fara.nearbymovies.db.dao.MovieDao
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Detail
import com.fara.nearbymovies.db.model.Preview

@Database(
    entities = [City::class, Cinema::class, Preview::class, Detail::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCinemaDao(): MovieDao
}
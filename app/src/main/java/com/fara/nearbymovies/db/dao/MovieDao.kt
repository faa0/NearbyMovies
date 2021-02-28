package com.fara.nearbymovies.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Preview

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(city: City, cinema: Cinema, preview: Preview)
}
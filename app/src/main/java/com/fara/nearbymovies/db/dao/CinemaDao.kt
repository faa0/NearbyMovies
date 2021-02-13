package com.fara.nearbymovies.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fara.nearbymovies.entity.Cinema


@Dao
interface CinemaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(cinema: Cinema): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(cinema: List<Cinema>)

    @Query("SELECT * FROM cinema_table")
    fun getAllCinemas(): LiveData<List<Cinema?>>

    @Delete
    fun deleteCinema(cinema: Cinema)
}
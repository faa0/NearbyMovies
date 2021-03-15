package com.fara.nearbymovies.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Detail
import com.fara.nearbymovies.db.model.Preview

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCinema(cinema: Cinema)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreview(preview: Preview)

    @Query("SELECT * FROM preview WHERE cinema_id = :id AND soon == 0")
    suspend fun getPreviewsById(id: Long): List<Preview>

    @Query("SELECT * FROM preview WHERE cinema_id = :id AND soon == 1")
    suspend fun getSoonById(id: Long): List<Preview>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailList(detailList: List<Detail>)

    @Query("SELECT * FROM detail WHERE cinema_id = :id")
    suspend fun getDetailList(id: Long): List<Detail>

    @Query("DELETE FROM preview WHERE cinema_id = :id AND soon = 0")
    suspend fun deleteAllPreviewByCinemaId(id: Long)

    @Query("DELETE FROM preview WHERE cinema_id = :id AND soon = 1")
    suspend fun deleteAllSoonByCinemaId(id: Long)

    @Query("SELECT city FROM city")
    suspend fun getCityNamesList(): List<String>

    @Query("SELECT cinema FROM cinema")
    suspend fun getCinemaNamesList(): List<String>
}
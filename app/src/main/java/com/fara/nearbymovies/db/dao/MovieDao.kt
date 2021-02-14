package com.fara.nearbymovies.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fara.nearbymovies.db.model.CompareMovie
import com.fara.nearbymovies.db.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(movie: List<Movie>)

    @Query("SELECT city, cinema, session FROM movies_table WHERE title = :title")
    fun getSessionByTitle(title: String): LiveData<List<CompareMovie>>
}
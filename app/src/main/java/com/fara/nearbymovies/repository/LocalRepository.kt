package com.fara.nearbymovies.repository

import com.fara.nearbymovies.db.AppDatabase
import com.fara.nearbymovies.db.model.Movie
import com.fara.nearbymovies.entity.Cinema

class LocalRepository(private val db: AppDatabase) {

    fun upsert(cinema: Cinema) = db.cinemaDao().upsert(cinema)

    fun upsertList(cinema: List<Cinema>) = db.cinemaDao().upsert(cinema)

    fun getCinemas() = db.cinemaDao().getAllCinemas()

    fun deleteCinema(cinema: Cinema) = db.cinemaDao().deleteCinema(cinema)

    fun upsertMovieListDetail(movie: List<Movie>) = db.movieDao().upsert(movie)
}
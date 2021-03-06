package com.fara.nearbymovies.repo

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.db.AppDatabase
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Preview
import javax.inject.Inject

class LocalRepo @Inject constructor(
    private val db: AppDatabase
) : ViewModel() {

    fun insert(city: City, cinema: Cinema, preview: Preview) =
        db.getCinemaDao().insert(city, cinema, preview)

    fun getPreviewsById(id: Long) = db.getCinemaDao().getPreviewsById(id)

    fun getSoonById(id: Long) = db.getCinemaDao().getSoonById(id)
}
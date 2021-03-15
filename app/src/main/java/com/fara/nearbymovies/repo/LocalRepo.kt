package com.fara.nearbymovies.repo

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.db.AppDatabase
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Detail
import com.fara.nearbymovies.db.model.Preview
import javax.inject.Inject

class LocalRepo @Inject constructor(
    private val db: AppDatabase
) : ViewModel() {

    suspend fun insertCity(city: City) = db.getCinemaDao().insertCity(city)

    suspend fun insertCinema(cinema: Cinema) = db.getCinemaDao().insertCinema(cinema)

    suspend fun insertPreview(preview: Preview) = db.getCinemaDao().insertPreview(preview)

    suspend fun getPreviewsById(id: Long) = db.getCinemaDao().getPreviewsById(id)

    suspend fun getSoonById(id: Long) = db.getCinemaDao().getSoonById(id)

    suspend fun insertDetail(detailList: List<Detail>) =
        db.getCinemaDao().insertDetailList(detailList)

    suspend fun getDetailList(id: Long) = db.getCinemaDao().getDetailList(id)

    suspend fun deleteAllPreviewByCinemaId(id: Long) =
        db.getCinemaDao().deleteAllPreviewByCinemaId(id)

    suspend fun deleteAllSoonByCinemaId(id: Long) = db.getCinemaDao().deleteAllSoonByCinemaId(id)

    suspend fun getCityNamesList() = db.getCinemaDao().getCityNamesList()

    suspend fun getCinemaNamesList() = db.getCinemaDao().getCinemaNamesList()
}
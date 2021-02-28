package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Preview
import com.fara.nearbymovies.repo.LocalRepo
import com.fara.nearbymovies.repo.RemoteRepo
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_TITLE
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_URL
import com.fara.nearbymovies.utils.Constants.Companion.ODESSA_BASE_TITLE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val remoteRepo: RemoteRepo,
    private val localRepo: LocalRepo
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            setCinemaCityLiveData()
            insertCinemaCityPreviewToDb()
        }
    }

    private val previewList = mutableListOf<Preview>()
    val previewLD = MutableLiveData<List<Preview>>()

    fun setCinemaCityLiveData() {
        val doc = Jsoup.connect(CINEMA_CITY_BASE_URL).get()
        previewLD.postValue(setCinemaCityToPreviewList(doc))
    }

    private fun insertCinemaCityPreviewToDb() {
        previewList.forEach {
            localRepo.insert(
                City(
                    id = 0,
                    city = ODESSA_BASE_TITLE
                ),
                Cinema(
                    id = 0,
                    city_id = 1,
                    cinema = CINEMA_CITY_BASE_TITLE,
                ),
                Preview(
                    id = 0,
                    cinema_id = 1,
                    title = it.title,
                    poster_url = it.poster_url,
                    movie_url = it.movie_url,
                    age = it.age
                )
            )
        }
    }

    private fun setCinemaCityToPreviewList(doc: Document): MutableList<Preview> {
        previewList.clear()
        val preview = doc.getElementsByClass("poster")
        preview.forEach {
            remoteRepo.apply {
                previewList += Preview(
                    id = 0,
                    cinema_id = 0,
                    title = getCinemaCityTitlePremiere(it),
                    poster_url = getCinemaCityPosterUrlPremiere(it),
                    movie_url = getCinemaCityMovieUrlPremiere(it),
                    age = getCinemaCityAgePremiere(it)
                )
            }
        }
        return previewList
    }
}
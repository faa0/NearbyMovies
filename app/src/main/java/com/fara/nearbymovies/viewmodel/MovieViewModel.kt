package com.fara.nearbymovies.viewmodel

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.nearbymovies.R
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
import com.fara.nearbymovies.db.model.Detail
import com.fara.nearbymovies.db.model.Preview
import com.fara.nearbymovies.repo.LocalRepo
import com.fara.nearbymovies.repo.RemoteRepo
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_ID
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_TITLE
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_URL
import com.fara.nearbymovies.utils.Constants.Companion.ODESSA_BASE_ID
import com.fara.nearbymovies.utils.Constants.Companion.ODESSA_BASE_TITLE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val remoteRepo: RemoteRepo,
    private val localRepo: LocalRepo,
    @ApplicationContext context: Context
) : ViewModel() {

    companion object {
        const val START_ID_FOR_PREVIEW = 1L
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val getCinemaCityPreviewFromDb = localRepo.getPreviewsById(CINEMA_CITY_BASE_ID)
            val getCinemaCitySoonFromDb = localRepo.getSoonById(CINEMA_CITY_BASE_ID)
            isCinemaCityNotEmpty(getCinemaCityPreviewFromDb, previewLD)
            isCinemaCityNotEmpty(getCinemaCitySoonFromDb, soonLD)

            if (checkInternetConnection(context)) {
                val doc = Jsoup.connect(CINEMA_CITY_BASE_URL).get()
                if (getCinemaCityPreviewList(doc) != getCinemaCityPreviewFromDb) {
                    previewLD.postValue(getCinemaCityPreviewList(doc))
                    insertCinemaCityPreviewToDb()
                }
                if (getCinemaCitySoonList(doc) != getCinemaCitySoonFromDb) {
                    soonLD.postValue(getCinemaCitySoonList(doc))
                    insertCinemaCitySoonToDb()
                }

                Log.d("tea", getCinemaCityDetailPreviewList().toString())
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.please_connect_to_the_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val previewLD = MutableLiveData<List<Preview>>()
    private val previewList = mutableListOf<Preview>()

    val soonLD = MutableLiveData<List<Preview>>()
    private val soonList = mutableListOf<Preview>()

    val detailPreviewLD = MutableLiveData<Detail>()
    var positionPreview = 0
    private lateinit var detailPremiere: Detail
    private val detailList = mutableListOf<Detail>()

    fun updateDetailPreview() = detailPreviewLD.postValue(getCinemaCityDetailPreview())

    private fun getCinemaCityDetailPreviewList(): List<Detail> {
        previewList.forEach {
            val doc = Jsoup.connect(it.movie_url).get()
            remoteRepo.apply {
                detailList += Detail(
                    preview_id = 0,
                    description = getCinemaCityDescription(doc),
                    year = getCinemaCityYear(doc),
                    country = getCinemaCityCountry(doc),
                    genre = getCinemaCityGenre(doc),
                    background = getCinemaCityBackground(doc),
                    video_url = getCinemaCityVideoUrl(doc)
                )
            }
        }
        return detailList
    }

    private fun getCinemaCityDetailPreview(): Detail {
        val doc = Jsoup.connect(previewList[positionPreview].movie_url).get()
        remoteRepo.apply {
            detailPremiere = Detail(
                description = getCinemaCityDescription(doc),
                year = getCinemaCityYear(doc),
                country = getCinemaCityCountry(doc),
                genre = getCinemaCityGenre(doc),
                background = getCinemaCityBackground(doc),
                video_url = getCinemaCityVideoUrl(doc)
            )
        }
        return detailPremiere
    }

    private fun isCinemaCityNotEmpty(list: List<Preview>, ld: MutableLiveData<List<Preview>>) {
        if (list != emptyList<Preview>()) ld.postValue(list)
    }

    private suspend fun insertCinemaCityPreviewToDb() {
        previewList.forEach {
            localRepo.insert(
                City(city = ODESSA_BASE_TITLE),
                Cinema(city_id = ODESSA_BASE_ID, cinema = CINEMA_CITY_BASE_TITLE),
                Preview(
                    cinema_id = CINEMA_CITY_BASE_ID,
                    title = it.title,
                    poster_url = it.poster_url,
                    movie_url = it.movie_url,
                    age = it.age,
                    soon = false
                )
            )
        }
    }

    private fun getCinemaCityPreviewList(doc: Document): List<Preview> {
        previewList.clear()
        val preview = doc.getElementsByClass("poster")
        var id = START_ID_FOR_PREVIEW
        preview.forEach {
            remoteRepo.apply {
                previewList += Preview(
                    id = id,
                    cinema_id = ODESSA_BASE_ID,
                    title = getCinemaCityTitlePremiere(it),
                    poster_url = getCinemaCityPosterUrlPremiere(it),
                    movie_url = getCinemaCityMovieUrlPremiere(it),
                    age = getCinemaCityAgePremiere(it),
                    soon = false
                )
            }
            id++
        }
        return previewList
    }

    private suspend fun insertCinemaCitySoonToDb() {
        soonList.forEach {
            localRepo.insert(
                City(city = ODESSA_BASE_TITLE),
                Cinema(city_id = ODESSA_BASE_ID, cinema = CINEMA_CITY_BASE_TITLE),
                Preview(
                    cinema_id = CINEMA_CITY_BASE_ID,
                    title = it.title,
                    poster_url = it.poster_url,
                    movie_url = it.movie_url,
                    date = it.date,
                    soon = true
                )
            )
        }
    }

    private fun getCinemaCitySoonList(doc: Document): List<Preview> {
        soonList.clear()
        val soon = doc.getElementsByClass("on-screen-soon")
        var id = previewList.size + 1L
        soon.forEach {
            remoteRepo.apply {
                soonList += Preview(
                    id = id,
                    cinema_id = ODESSA_BASE_ID,
                    title = getCinemaCityTitleSoon(it),
                    poster_url = getCinemaCityPosterUrlSoon(it),
                    movie_url = getCinemaCityMovieUrlSoon(it),
                    date = getCinemaCityDateSoon(it),
                    soon = true
                )
            }
            id++
        }
        return soonList
    }

    private fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
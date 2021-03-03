package com.fara.nearbymovies.viewmodel

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fara.nearbymovies.R
import com.fara.nearbymovies.db.model.Cinema
import com.fara.nearbymovies.db.model.City
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
            val getCinemaCityMoviesFromDb = localRepo.getMoviesById(CINEMA_CITY_BASE_ID)
            if (getCinemaCityMoviesFromDb != emptyList<Preview>())
                previewLD.postValue(getCinemaCityMoviesFromDb)

            if (hasInternetConnection(context)) {
                val doc = Jsoup.connect(CINEMA_CITY_BASE_URL).get()
                if (setCinemaCityToPreviewList(doc) != getCinemaCityMoviesFromDb) {
                    previewLD.postValue(setCinemaCityToPreviewList(doc))
                    insertCinemaCityPreviewToDb()
                }
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

    private fun insertCinemaCityPreviewToDb() {
        previewList.forEach {
            localRepo.insert(
                City(city = ODESSA_BASE_TITLE),
                Cinema(city_id = ODESSA_BASE_ID, cinema = CINEMA_CITY_BASE_TITLE),
                Preview(
                    cinema_id = CINEMA_CITY_BASE_ID,
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
        var id = START_ID_FOR_PREVIEW
        preview.forEach {
            remoteRepo.apply {
                previewList += Preview(
                    id = id,
                    cinema_id = ODESSA_BASE_ID,
                    title = getCinemaCityTitlePremiere(it),
                    poster_url = getCinemaCityPosterUrlPremiere(it),
                    movie_url = getCinemaCityMovieUrlPremiere(it),
                    age = getCinemaCityAgePremiere(it)
                )
            }
            id++
        }
        return previewList
    }

    private fun hasInternetConnection(context: Context): Boolean {
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
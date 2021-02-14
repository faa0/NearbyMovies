package com.fara.nearbymovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fara.nearbymovies.db.model.Movie
import com.fara.nearbymovies.entity.Cinema
import com.fara.nearbymovies.entity.Detail
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.LocalRepository
import com.fara.nearbymovies.repository.RemoteRepository
import com.fara.nearbymovies.utils.Constants
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_URL
import com.fara.nearbymovies.utils.Constants.Companion.MAYAK_ZP
import com.fara.nearbymovies.utils.Constants.Companion.MULTIPLEX_ZP
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MovieViewModel(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    app: Application
) : AndroidViewModel(app) {

    init {
        GlobalScope.launch {
            setCinemaCityLiveData()
        }
    }

    private val premiereList = mutableListOf<Cinema>()
    private lateinit var detailPremiere: Detail
    val premiereLiveData = MutableLiveData<List<Cinema>>()
    val detailLiveDataPremiere = MutableLiveData<Detail>()
    var positionPremiere = 0

    private val soonList = mutableListOf<Soon>()
    private lateinit var detailSoon: Detail
    val soonLiveData = MutableLiveData<List<Soon>>()
    val detailLiveDataSoon = MutableLiveData<Detail>()
    var positionSoon = 0

    var cinema = 0

    fun getSessionByTitle(title: String) = localRepository.getSessionByTitle(title)

    fun insertCinema(cinema: Cinema) = localRepository.upsert(cinema)

    fun insertCinemaList(cinema: List<Cinema>) = localRepository.upsertList(cinema)

    fun getCinemas() = localRepository.getCinemas()

    fun deleteCinema(cinema: Cinema) = localRepository.deleteCinema(cinema)

    fun setMayakLiveData() {
        val doc = Jsoup.connect(MAYAK_ZP).get()
        premiereLiveData.postValue(setMayakPremiereList(doc))
    }

    fun setMultiplexLiveData() {
        val doc = Jsoup.connect(MULTIPLEX_ZP).get()
        premiereLiveData.postValue(setMultiplexPremiereList(doc))
    }

    fun setCinemaCityLiveData() {
        val doc = Jsoup.connect(CINEMA_CITY_BASE_URL).get()
        soonLiveData.postValue(setCinemaCitySoonList(doc))
        premiereLiveData.postValue(setCinemaCityPremiereList(doc))
    }

    fun updateDetailPremiere() {
        when (cinema) {
            0 -> {
                detailLiveDataPremiere.postValue(setCinemaCityDetailToPremiere())

            }
            1 -> {
                detailLiveDataPremiere.postValue(setMultiplexDetailToPremiere())

            }
            2 -> {
                detailLiveDataPremiere.postValue(setMayakDetailToPremiere())
            }
        }
    }

    fun updateDetailSoon() = detailLiveDataSoon.postValue(setCinemaCityDetailToSoon())

    fun setMayakListDetailToDb() {
        val movieList = mutableListOf<Movie>()
        for (it in premiereList) {
            val doc = Jsoup.connect(it.movie_url).get()
            val docSchedule = Jsoup.connect(Constants.MAYAK_ZP_SCHEDULE).get()
                .getElementsByClass("showstimes")[positionPremiere]
            remoteRepository.apply {
                movieList += Movie(
                    "Zaporozhye",
                    "Mayakovskogo",
                    getMayakovskogoTitle(doc),
                    getMayakovskogoSchedule(docSchedule)
                )
                localRepository.upsertMovieListDetail(movieList)
            }
        }
    }

    private fun setMayakDetailToPremiere(): Detail {
        val doc = Jsoup.connect(premiereList[positionPremiere].movie_url).get()
        val docSchedule = Jsoup.connect(Constants.MAYAK_ZP_SCHEDULE).get()
            .getElementsByClass("showstimes")[positionPremiere]
        remoteRepository.apply {
            detailPremiere = Detail(
                null,
                getMayakovskogoDescription(doc),
                null,
                getMayakovskogoYear(doc),
                getMayakovskogoCountry(doc),
                getMayakovskogoGenre(doc),
                getMayakovskogoSchedule(docSchedule)
            )
        }
        return detailPremiere
    }

    private fun setMayakPremiereList(doc: Document): MutableList<Cinema> {
        premiereList.clear()
        var count = 0
        while (count < 2 * RemoteRepository().getMayakovskogoItemSize()) {
            val premiere = doc.getElementsByClass("film-title-list")[count / 2]
            val poster = doc.getElementsByClass("img-holder").select("a")[count]
            premiereList += Cinema(
                "Mayakovskogo",
                "Zaporozhye",
                remoteRepository.getMayakovskogoTitlePremiere(premiere),
                remoteRepository.getMayakovskogoPosterUrlPremiere(poster),
                remoteRepository.getMayakovskogoMovieUrlPremiere(premiere),
                null
            )
            count += 2
        }
        return premiereList
    }

    fun setMultiplexListDetailToDb() {
        val movieList = mutableListOf<Movie>()
        for (it in premiereList) {
            val doc = Jsoup.connect(it.movie_url).get()
            remoteRepository.apply {
                movieList += Movie(
                    "Zaporozhye",
                    "Multiplex",
                    getMultiplexTitle(doc),
                    getMultiplexSchedule(doc)
                )
                localRepository.upsertMovieListDetail(movieList)
            }
        }
    }

    private fun setMultiplexDetailToPremiere(): Detail {
        val doc = Jsoup.connect(premiereList[positionPremiere].movie_url).get()
        remoteRepository.apply {
            detailPremiere = Detail(
                null,
                getMultiplexDescription(doc),
                getMultiplexVideoUrl(doc),
                getMultiplexYear(doc),
                getMultiplexCountry(doc),
                getMultiplexGenre(doc),
                getMultiplexSchedule(doc)
            )
        }
        return detailPremiere
    }

    private fun setMultiplexPremiereList(doc: Document): MutableList<Cinema> {
        premiereList.clear()
        var count = 0
        while (count < 2 * RemoteRepository().getMultiplexItemSize()) {
            val premiere = doc.getElementsByClass("cinema_inside sch_date")
                .select("div.film")
                .select("a")[count]
            premiereList += Cinema(
                "Multiplex",
                "Zaporozhye",
                remoteRepository.getMultiplexTitlePremiere(premiere),
                remoteRepository.getMultiplexPosterUrlPremiere(premiere),
                remoteRepository.getMultiplexMovieUrlPremiere(premiere),
                null
            )
            count += 2
        }
        return premiereList
    }

    private fun setCinemaCityDetailToSoon(): Detail {
        val doc = Jsoup.connect(soonList[positionSoon].movie_url).get()
        remoteRepository.apply {
            detailSoon = Detail(
                getCinemaCityBackground(doc),
                getCinemaCityDescription(doc),
                getCinemaCityVideoUrl(doc),
                getCinemaCityYear(doc),
                getCinemaCityCountry(doc),
                getCinemaCityGenre(doc),
                null
            )
        }
        return detailSoon
    }

    fun setCinemaCityListDetailToDb() {
        val movieList = mutableListOf<Movie>()
        for (it in premiereList) {
            val doc = Jsoup.connect(it.movie_url).get()
            remoteRepository.apply {
                movieList += Movie(
                    "Odessa",
                    "CinemaCity",
                    getCinemaCityTitle(doc),
                    getCinemaCitySchedule(doc)
                )
                localRepository.upsertMovieListDetail(movieList)
            }
        }
    }

    private fun setCinemaCityDetailToPremiere(): Detail {
        val doc = Jsoup.connect(premiereList[positionPremiere].movie_url).get()
        remoteRepository.apply {
            detailPremiere = Detail(
                getCinemaCityBackground(doc),
                getCinemaCityDescription(doc),
                getCinemaCityVideoUrl(doc),
                getCinemaCityYear(doc),
                getCinemaCityCountry(doc),
                getCinemaCityGenre(doc),
                getCinemaCitySchedule(doc)
            )
        }
        return detailPremiere
    }

    private fun setCinemaCityPremiereList(doc: Document): MutableList<Cinema> {
        premiereList.clear()
        val premiere = doc.getElementsByClass("poster")
        for (element in premiere)
            premiereList += Cinema(
                "CinemaCity",
                "Odessa",
                remoteRepository.getCinemaCityTitlePremiere(element),
                remoteRepository.getCinemaCityPosterUrlPremiere(element),
                remoteRepository.getCinemaCityMovieUrlPremiere(element),
                remoteRepository.getCinemaCityAgePremiere(element)
            )
        return premiereList
    }

    private fun setCinemaCitySoonList(doc: Document): MutableList<Soon> {
        val soon = doc.getElementsByClass("on-screen-soon")
        premiereList.clear()
        for (element in soon) {
            soonList += Soon(
                remoteRepository.getCinemaCityTitleSoon(element),
                remoteRepository.getCinemaCityPosterUrlSoon(element),
                remoteRepository.getCinemaCityDateSoon(element),
                remoteRepository.getCinemaCityMovieUrlSoon(element)
            )
        }
        return soonList
    }
}
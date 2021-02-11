package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Detail
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.repository.MultiplexRepository
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_URL
import com.fara.nearbymovies.utils.Constants.Companion.MULTIPLEX_ZP
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val multiplexRepository: MultiplexRepository
) : ViewModel() {

    init {
        GlobalScope.launch { setDataToLiveData() }
    }

    private val premiereList = mutableListOf<Premiere>()
    private lateinit var detailPremiere: Detail
    val premiereLiveData = MutableLiveData<List<Premiere>>()
    val detailLiveDataPremiere = MutableLiveData<Detail>()
    var positionPremiere = 0

    private val soonList = mutableListOf<Soon>()
    private lateinit var detailSoon: Detail
    val soonLiveData = MutableLiveData<List<Soon>>()
    val detailLiveDataSoon = MutableLiveData<Detail>()
    var positionSoon = 0

    fun setMultiplexLiveData() {
        val doc = Jsoup.connect(MULTIPLEX_ZP).get()
        premiereLiveData.postValue(setMultiplexPremiereList(doc))
    }

    fun setDataToLiveData() {
        val doc = Jsoup.connect(CINEMA_CITY_BASE_URL).get()
        soonLiveData.postValue(setDataToSoonList(doc))
        premiereLiveData.postValue(setDataToPremiereList(doc))
    }

    fun updateDetailPremiere() = detailLiveDataPremiere.postValue(setDetailToPremiere())

    fun updateDetailSoon() = detailLiveDataSoon.postValue(setDetailToSoon())

    private fun setMultiplexPremiereList(doc: Document): MutableList<Premiere> {
        premiereList.clear()
        var count = 0
        while (count < 2 * MultiplexRepository().getItemSize()) {
            val premiere = doc.getElementsByClass("cinema_inside sch_date")
                .select("div.film")
                .select("a")[count]
            premiereList += Premiere(
                multiplexRepository.getTitlePremiere(premiere),
                multiplexRepository.getPosterUrlPremiere(premiere),
                multiplexRepository.getMovieUrlPremiere(premiere),
                null
            )
            count += 2
        }
        return premiereList
    }

    private fun setDetailToSoon(): Detail {
        val doc = Jsoup.connect(soonList[positionSoon].movie_url).get()
        movieRepository.apply {
            detailSoon = Detail(
                getBackground(doc),
                getDescription(doc),
                getVideoUrl(doc),
                getYear(doc),
                getCountry(doc),
                getGenre(doc),
                null
            )
        }
        return detailSoon
    }

    private fun setDetailToPremiere(): Detail {
        val doc = Jsoup.connect(premiereList[positionPremiere].movie_url).get()
        movieRepository.apply {
            detailPremiere = Detail(
                getBackground(doc),
                getDescription(doc),
                getVideoUrl(doc),
                getYear(doc),
                getCountry(doc),
                getGenre(doc),
                getSchedule(doc)
            )
        }
        return detailPremiere
    }

    private fun setDataToPremiereList(doc: Document): MutableList<Premiere> {
        premiereList.clear()
        val premiere = doc.getElementsByClass("poster")
        for (element in premiere)
            premiereList += Premiere(
                movieRepository.getTitlePremiere(element),
                movieRepository.getPosterUrlPremiere(element),
                movieRepository.getMovieUrlPremiere(element),
                movieRepository.getAgePremiere(element)
            )
        return premiereList
    }

    private fun setDataToSoonList(doc: Document): MutableList<Soon> {
        val soon = doc.getElementsByClass("on-screen-soon")
        premiereList.clear()
        for (element in soon) {
            soonList += Soon(
                movieRepository.getTitleSoon(element),
                movieRepository.getPosterUrlSoon(element),
                movieRepository.getDateSoon(element),
                movieRepository.getMovieUrlSoon(element)
            )
        }
        return soonList
    }
}
package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Detail
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    init {
        GlobalScope.launch {
            setDataToLiveData()
        }
    }

    private val premiereList = mutableListOf<Premiere>()
    private val urlListPremiere = mutableListOf<String>()
    private lateinit var detailPremiere: Detail
    val premiereLiveData = MutableLiveData<MutableList<Premiere>>()
    val detailLiveDataPremiere = MutableLiveData<Detail>()
    var positionPremiere = 0

    private val soonList = mutableListOf<Soon>()
    private val urlListSoon = mutableListOf<String>()
    private lateinit var detailSoon: Detail
    val soonLiveData = MutableLiveData<List<Soon>>()
    val detailLiveDataSoon = MutableLiveData<Detail>()
    var positionSoon = 0

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        soonLiveData.postValue(setDataToSoonList(doc))
        premiereLiveData.postValue(setDataToPremiereList(doc))
        detailLiveDataPremiere.postValue(setDetailToPremiere())
        detailLiveDataSoon.postValue(setDetailToSoon())
    }


    fun updateDetailPremiere() {
        detailLiveDataPremiere.postValue(setDetailToPremiere())
    }

    fun updateDetailSoon() {
        detailLiveDataSoon.postValue(setDetailToSoon())
    }

    private fun setDetailToSoon(): Detail {
        val doc = Jsoup.connect(setDataToUrlListSoon()[positionSoon]).get()
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
        val doc = Jsoup.connect(setDataToUrlListPremiere()[positionPremiere]).get()
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

    private fun setDataToUrlListPremiere(): List<String> {
        for (i in premiereList) i.movie_url.let { urlListPremiere.add(it) }
        return urlListPremiere
    }

    private fun setDataToUrlListSoon(): List<String> {
        for (i in soonList) i.movie_url.let { urlListSoon.add(it) }
        return urlListSoon
    }

    private fun setDataToPremiereList(doc: Document): MutableList<Premiere> {
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
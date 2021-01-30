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
    private val detailList = mutableListOf<Detail>()
    val premiereLiveData = MutableLiveData<MutableList<Premiere>>()
    val detailLiveData = MutableLiveData<MutableList<Detail>>()

    private lateinit var detail: Detail
    private val soonList = mutableListOf<Soon>()
    private val urlList = mutableListOf<String>()
    val soonLiveData = MutableLiveData<List<Soon>>()
    val detailLiveDataSoon = MutableLiveData<Detail>()
    var position = 0

    fun setDataToDetailLiveData() {
        detailLiveDataSoon.postValue(setDetailMovie())
    }

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        premiereLiveData.postValue(setDataToPremiereList(doc))
        soonLiveData.postValue(setDataToSoonList(doc))
        detailLiveData.postValue(setDetailToList())
        setDataToDetailLiveData()
    }

    private fun setDetailToList(): MutableList<Detail> {
        for (i in setDataToUrlList()) {
            val doc = Jsoup.connect(i).get()
            movieRepository.apply {
                detailList += Detail(
                    getBackground(doc),
                    getDescription(doc),
                    getVideoUrl(doc),
                    getYear(doc),
                    getCountry(doc),
                    getGenre(doc),
                    getSchedule(doc)
                )
            }
        }
        return detailList
    }

    private fun setDetailMovie(): Detail {
        val doc = Jsoup.connect(setDataToUrlListSoon()[position]).get()
        movieRepository.apply {
            detail = Detail(
                getBackground(doc),
                getDescription(doc),
                getVideoUrl(doc),
                getYear(doc),
                getCountry(doc),
                getGenre(doc),
                null
            )
        }
        return detail
    }

    private fun setDataToUrlList(): List<String> {
        for (i in premiereList) i.movie_url.let { urlListPremiere.add(it) }
        return urlListPremiere
    }

    private fun setDataToUrlListSoon(): List<String> {
        for (i in soonList) i.movie_url.let { urlList.add(it) }
        return urlList
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
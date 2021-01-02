package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Detail
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MovieViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    init {
        GlobalScope.launch {
            setDataToLiveData()
        }
    }

    private val premiereList = mutableListOf<Premiere>()
    private val urlList = mutableListOf<String>()
    private val detailList = mutableListOf<Detail>()
    val premiereLiveData = MutableLiveData<MutableList<Premiere>>()
    val detailLiveData = MutableLiveData<MutableList<Detail>>()

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        premiereLiveData.postValue(setDataToPremiereList(doc))
        detailLiveData.postValue(setDetailToList())
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

    private fun setDataToUrlList(): List<String> {
        for (i in premiereList) urlList.add(i.movie_url)
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
}
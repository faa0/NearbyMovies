package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    init {
        GlobalScope.launch {
            delay(500)
            setDataToLiveData()
            println("I'm working in thread ${Thread.currentThread().name}")
        }
    }

    private val premiereList = mutableListOf<Premiere>()
    val premiereLiveData = MutableLiveData<List<Premiere>>()

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        premiereLiveData.postValue(setDataToPremiereList(doc))
    }

    private fun setDataToPremiereList(doc: Document): List<Premiere> {
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
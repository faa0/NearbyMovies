package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val soonList = mutableListOf<Soon>()
    private val premiereList = mutableListOf<Premiere>()
    val soonLiveData = MutableLiveData<List<Soon>>()
    val premiereLiveData = MutableLiveData<List<Premiere>>()

    init {
        GlobalScope.launch {
            getData()
        }
    }

    private fun getData() {
        val doc: Document = Jsoup.connect(BASE_URL).get()
        premiereLiveData.postValue(setDataToPremiereList(doc))
        soonLiveData.postValue(setDataToSoonList(doc))
    }

    private fun setDataToSoonList(doc: Document): List<Soon> {
        val soon: Elements = doc.getElementsByClass("on-screen-soon")
        for (element in soon) {
            soonList.add(
                Soon(
                    movieRepository.getTitleSoon(element),
                    movieRepository.getPosterUrlSoon(element),
                    movieRepository.getDateSoon(element),
                    movieRepository.getMovieUrlSoon(element)
                )
            )
        }
        return soonList
    }

    private fun setDataToPremiereList(doc: Document): List<Premiere> {
        val premiere: Elements = doc.getElementsByClass("poster")
        for (element in premiere) {
            premiereList.add(
                Premiere(
                    movieRepository.getTitlePremiere(element),
                    movieRepository.getPosterUrlPremiere(element),
                    movieRepository.getMovieUrlPremiere(element),
                    movieRepository.getAgePremiere(element)
                )
            )
        }
        return premiereList
    }
}
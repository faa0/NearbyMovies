package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.SoonRepository
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SoonViewModel(
    private val soonRepository: SoonRepository,
) : ViewModel() {

    init {
        GlobalScope.launch {
            delay(500)
            setDataToLiveData()
            println("I'm working in thread ${Thread.currentThread().name}")
        }
    }

    private val soonList = mutableListOf<Soon>()
    val soonLiveData = MutableLiveData<List<Soon>>()

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        soonLiveData.postValue(setDataToSoonList(doc))
    }

    private fun setDataToSoonList(doc: Document): List<Soon> {
        val soon = doc.getElementsByClass("on-screen-soon")
        for (element in soon) {
            soonList += Soon(
                soonRepository.getTitleSoon(element),
                soonRepository.getPosterUrlSoon(element),
                soonRepository.getDateSoon(element),
                soonRepository.getMovieUrlSoon(element)
            )
        }
        return soonList
    }
}
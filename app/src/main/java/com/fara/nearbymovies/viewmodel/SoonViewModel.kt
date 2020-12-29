package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.SoonRepository
import com.fara.nearbymovies.utils.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SoonViewModel(
    private val soonRepository: SoonRepository,
) : ViewModel() {

    init {
        GlobalScope.launch {
            setDataToLiveData()
        }
    }

    private val soonList = mutableListOf<Soon>()
    val soonLiveData = MutableLiveData<List<Soon>>()

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(Constants.BASE_URL).get()
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
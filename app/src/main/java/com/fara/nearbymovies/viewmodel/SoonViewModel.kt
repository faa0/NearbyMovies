package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Detail
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.repository.SoonRepository
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
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
    private val urlList = mutableListOf<String>()
    private val detailList = mutableListOf<Detail>()
    val soonLiveData = MutableLiveData<List<Soon>>()
    val detailLiveData = MutableLiveData<MutableList<Detail>>()

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        soonLiveData.postValue(setDataToSoonList(doc))
        detailLiveData.postValue(setDetailToList())
    }

    private fun setDetailToList(): MutableList<Detail> {
        for (i in setDataToUrlList()) {
            val doc = Jsoup.connect(i).get()
            soonRepository.apply {
                detailList += Detail(
                    getBackground(doc),
                    getDescription(doc),
                    getVideoUrl(doc),
                    getYear(doc),
                    getCountry(doc),
                    getGenre(doc),
                    null
                )
            }
        }
        return detailList
    }

    private fun setDataToUrlList(): List<String> {
        for (i in soonList) urlList.add(i.movie_url)
        return urlList
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
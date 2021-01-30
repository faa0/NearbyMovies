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

    private lateinit var detail: Detail
    private val soonList = mutableListOf<Soon>()
    private val urlList = mutableListOf<String>()
    val soonLiveData = MutableLiveData<List<Soon>>()
    val detailLiveData = MutableLiveData<Detail>()
    var position = 0

    fun setDataToDetailLiveData() {
        detailLiveData.postValue(setDetailMovie())
    }

    private fun setDataToLiveData() {
        val doc = Jsoup.connect(BASE_URL).get()
        soonLiveData.postValue(setDataToSoonList(doc))
        setDataToDetailLiveData()
    }

    private fun setDetailMovie(): Detail {
        val doc = Jsoup.connect(setDataToUrlList()[position]).get()
        soonRepository.apply {
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
        for (i in soonList) i.movie_url.let { urlList.add(it) }
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
package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.repository.DetailRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DetailViewModel(
    private val detailRepository: DetailRepository,
) : ViewModel() {

    val premiereLiveData = MutableLiveData<Premiere>()

    fun getGenre(): String {
        val url = premiereLiveData.value?.movie_url
        val doc: Document = Jsoup.connect(url).get()
        return detailRepository.getGenre(doc)
    }

    fun getCountry(): String {
        val url = premiereLiveData.value?.movie_url
        val doc: Document = Jsoup.connect(url).get()
        return detailRepository.getCountry(doc)
    }

    fun getYear(): String {
        val url = premiereLiveData.value?.movie_url
        val doc: Document = Jsoup.connect(url).get()
        return detailRepository.getYear(doc)
    }

    fun getVideoUrl(): String {
        val url = premiereLiveData.value?.movie_url
        val doc: Document = Jsoup.connect(url).get()
        return detailRepository.getVideoUrl(doc)
    }

    fun getDescription(): String {
        val url = premiereLiveData.value?.movie_url
        val doc: Document = Jsoup.connect(url).get()
        return detailRepository.getDescription(doc)
    }

    fun getBackground(): String {
        val url = premiereLiveData.value?.movie_url
        val doc: Document = Jsoup.connect(url).get()
        return detailRepository.getBackground(doc)
    }
}
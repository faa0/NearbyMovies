package com.fara.nearbymovies.repository

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import org.jsoup.nodes.Element

class MovieRepository : ViewModel() {

    fun getTitleSoon(element: Element): String {
        return element.select("div.on-screen-soon")
            .select("div.on-screen-soon__title")
            .text()
    }

    fun getPosterUrlSoon(element: Element): String {
        return (BASE_URL + element.select("div.poster-small")
            .select("img")
            .attr("src"))
    }

    fun getDateSoon(element: Element): String {
        return element.select("div.on-screen-soon")
            .select("div.on-screen-soon__date")
            .text()
    }

    fun getMovieUrlSoon(element: Element): String {
        return (BASE_URL + element.select("a")
            .attr("href"))
    }

    fun getTitlePremiere(element: Element): String {
        return element.select("img.poster__img")
            .attr("alt")
            .replace("фільм", "")
            .replace("постер", "").trim()
    }

    fun getPosterUrlPremiere(element: Element): String {
        return (BASE_URL + element.select("img.poster__img")
            .attr("src"))
    }

    fun getMovieUrlPremiere(element: Element): String {
        return element.select("a")
            .attr("href")
    }

    fun getAgePremiere(element: Element): String {
        return element.select("div.poster__info")
            .select("a.poster__name")
            .select("span.age-limitation")
            .text()
    }
}
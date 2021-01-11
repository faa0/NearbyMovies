package com.fara.nearbymovies.repository

import com.fara.nearbymovies.utils.Constants
import org.jsoup.nodes.Element

class SoonRepository {

    fun getTitleSoon(element: Element): String {
        return element.select("div.on-screen-soon")
            .select("div.on-screen-soon__title")
            .text()
    }

    fun getPosterUrlSoon(element: Element): String {
        return (Constants.BASE_URL + element.select("div.poster-small")
            .select("img")
            .attr("src"))
    }

    fun getDateSoon(element: Element): String {
        return element.select("div.on-screen-soon")
            .select("div.on-screen-soon__date")
            .text()
    }

    fun getMovieUrlSoon(element: Element): String {
        return (Constants.BASE_URL + element.select("a")
            .attr("href"))
    }
}
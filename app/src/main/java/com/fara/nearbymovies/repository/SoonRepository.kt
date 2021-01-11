package com.fara.nearbymovies.repository

import com.fara.nearbymovies.utils.Constants
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class SoonRepository {

    fun getGenre(doc: Document): String {
        return doc.getElementsByClass("movie__info")
            .select("div.movie__short-description")
            .select("div.movie__about")
            .select("p")[2].text()
    }

    fun getCountry(doc: Document): String {
        return doc.getElementsByClass("movie__info")
            .select("div.movie__short-description")
            .select("div.movie__about")
            .select("p")[1].text()
    }

    fun getYear(doc: Document): String {
        return doc.getElementsByClass("movie__info")
            .select("div.movie__short-description")
            .select("div.movie__about")
            .select("p")[0].text()
    }

    fun getVideoUrl(doc: Document): String {
        val elements = doc.getElementsByClass("movie-video-container")
        return elements
            .select("div.movie-video")
            .select("div.player")
            .attr("data-property")
            .replace("{videoURL:'", "")
            .replace(
                "',containment:'self', showYTLogo: false, showControls:true, " +
                        "autoPlay:false, loop:false, vol:50, mute:false, startAt:0," +
                        " stopAt:296, " + "opacity:1, quality:'large', " +
                        "optimizeDisplay:true}", ""
            )
    }

    fun getDescription(doc: Document): String {
        val elements = doc.getElementsByClass("movie__description")
        var desc = ""
        for (e in elements) {
            desc = e.select("div")
                .text()
        }
        return desc
    }

    fun getBackground(doc: Document): String {
        val elements = doc.getElementsByClass("movie-video-container")
        return (Constants.BASE_URL + elements
            .select("img.movie-video-container__img")
            .attr("src"))
    }

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
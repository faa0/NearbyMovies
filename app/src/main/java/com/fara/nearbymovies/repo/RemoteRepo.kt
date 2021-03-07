package com.fara.nearbymovies.repo

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.db.model.Session
import com.fara.nearbymovies.utils.Constants.Companion.CINEMA_CITY_BASE_URL
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class RemoteRepo @Inject constructor() : ViewModel() {

    //*CinemaCity

    fun getCinemaCitySchedule(doc: Document): MutableList<Session> {
        val listOfSessions = mutableListOf<Session>()
        repeat(getCinemaCityListOfSessions(doc).indices.count()) {
            listOfSessions += Session(
                getCinemaCityListOfSessions(doc)[it],
                getCinemaCityListOfTimes(doc)[it]
            )
        }
        return listOfSessions
    }

    private fun getCinemaCityListOfTimes(doc: Document): List<String> {
        val listOfTimes = mutableListOf<String>()
        repeat(getCinemaCityListOfSessions(doc).indices.count()) {
            val elements = doc.getElementsByClass("session__block")[it]
            listOfTimes += elements.select("div.session__schedule").text()
                //need to delete space only
                .replace("грн VIP ", "грнVIP\n")
                //need to be removed if the line is last
                .replace("грн VIP", "грнVIP")
                .replace("грн ", "грн\n")
                .replace("грнVIP", "грн VIP")
        }
        return listOfTimes
    }

    private fun getCinemaCityListOfSessions(doc: Document): List<String> {
        val elements = doc.getElementsByClass("session__block")
        val listOfSessions = mutableListOf<String>()
        elements.forEach { listOfSessions += it.select("div.session__type").text() }
        return listOfSessions
    }

    fun getCinemaCityGenre(doc: Document) = doc
        .getElementsByClass("movie__info")
        .select("div.movie__short-description")
        .select("div.movie__about")
        .select("p")[2]
        .text()

    fun getCinemaCityCountry(doc: Document) = doc
        .getElementsByClass("movie__info")
        .select("div.movie__short-description")
        .select("div.movie__about")
        .select("p")[1]
        .text()

    fun getCinemaCityYear(doc: Document) = doc
        .getElementsByClass("movie__info")
        .select("div.movie__short-description")
        .select("div.movie__about")
        .select("p")[0]
        .text()

    fun getCinemaCityVideoUrl(doc: Document): String {
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

    fun getCinemaCityDescription(doc: Document): String {
        val elements = doc.getElementsByClass("movie__description")
        var desc = ""
        elements.forEach { desc = it.select("div").text() }
        return desc
    }

    fun getCinemaCityBackground(doc: Document): String {
        val elements = doc.getElementsByClass("movie-video-container")
        return (CINEMA_CITY_BASE_URL + elements
            .select("img.movie-video-container__img")
            .attr("src"))
    }

    fun getCinemaCityTitle(doc: Document) = doc
        .getElementsByClass("movie-video-container__name-ukr")
        .text()

    fun getCinemaCityTitlePremiere(element: Element) = element
        .select("img.poster__img")
        .attr("alt")
        .replace("фільм", "")
        .replace("постер", "")
        .trim()

    fun getCinemaCityPosterUrlPremiere(element: Element) = CINEMA_CITY_BASE_URL + element
        .select("img.poster__img")
        .attr("src")

    fun getCinemaCityMovieUrlPremiere(element: Element) = element
        .select("a")
        .attr("href")

    fun getCinemaCityAgePremiere(element: Element) = element
        .select("div.poster__info")
        .select("a.poster__name")
        .select("span.age-limitation")
        .text()

    fun getCinemaCityMovieUrlSoon(element: Element) = CINEMA_CITY_BASE_URL + element
        .select("a")
        .attr("href")

    fun getCinemaCityTitleSoon(element: Element) = element
        .select("div.on-screen-soon")
        .select("div.on-screen-soon__title")
        .text()

    fun getCinemaCityPosterUrlSoon(element: Element) = CINEMA_CITY_BASE_URL + element
        .select("div.poster-small")
        .select("img")
        .attr("src")

    fun getCinemaCityDateSoon(element: Element) = element
        .select("div.on-screen-soon")
        .select("div.on-screen-soon__date")
        .text()
}
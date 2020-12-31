package com.fara.nearbymovies.repository

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Session
import com.fara.nearbymovies.utils.Constants.Companion.BASE_URL
import org.jsoup.nodes.Document

class DetailRepository : ViewModel() {

    fun getSchedule(doc: Document): MutableList<Session> {
        val listOfSessions = mutableListOf<Session>()
        for (i in getListOfSessions(doc).indices) {
            listOfSessions += Session(getListOfSessions(doc)[i], getListOfTimes(doc)[i])
        }
        return listOfSessions
    }

    private fun getListOfTimes(doc: Document): List<String> {
        val listOfTimes = mutableListOf<String>()
        for (i in getListOfSessions(doc).indices) {
            val elements = doc.getElementsByClass("session__block")[i]
            listOfTimes += elements.select("div.session__schedule").text()
                //need to delete space only
                .replace("грн VIP ", "грнVIP\n")
                .replace("грн ", "грн\n")
                .replace("грнVIP", "грн VIP")
        }
        return listOfTimes
    }

    private fun getListOfSessions(doc: Document): List<String> {
        val elements = doc.getElementsByClass("session__block")
        val listOfSessions = mutableListOf<String>()
        for (e in elements) {
            listOfSessions += e.select("div.session__type").text()
        }
        return listOfSessions
    }

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
        return (BASE_URL + elements
            .select("img.movie-video-container__img")
            .attr("src"))
    }
}
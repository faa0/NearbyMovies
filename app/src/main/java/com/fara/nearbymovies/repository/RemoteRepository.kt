package com.fara.nearbymovies.repository

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Session
import com.fara.nearbymovies.utils.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class RemoteRepository : ViewModel() {

    //*Mayakovskogo

    fun getMayakovskogoSchedule(element: Element): MutableList<Session> {
        val sessionsWithTimeList = element.text()
            .replaceFirst(" лютого", "лютого")
            .replaceAfter(" лютого", "")
            .replaceAfterLast("грн", "")
            .replace("Сьогодні, ", "")
            .replaceBefore(",", "")
            .replace(", ", "")
            .replaceFirst(" ", "/")
            .replaceBefore("/", "")
            .replace("/", "")
            .replace("грн ", "грн, ")
            .replace(", ", "/")
            .replace(" ", " вiд ")
            .replace("…", "-")
            .replace("/", ",")
            .split(",")

        val twoDList = mutableListOf<Session>()
        val resultList = mutableListOf<Session>()

        for (it in sessionsWithTimeList) {
            twoDList += Session("2D", it)
        }

        var twoDString = ""
        for (s in twoDList) {
            twoDString += s.time_price + "\n"
        }
        if (twoDString.isNotEmpty()) resultList.add(Session("2D", twoDString))

        return resultList
    }

    fun getMayakovskogoGenre(doc: Document): String {
        return doc.getElementsByClass("film-block")
            .select("dl.film-data-list")
            .select("dd")[0].text()
    }

    fun getMayakovskogoCountry(doc: Document): String {
        return doc.getElementsByClass("aside-info")
            .select("dl.film-data-list")
            .select("dd")[2].text()
    }

    fun getMayakovskogoYear(doc: Document): String {
        return doc.getElementsByClass("aside-info")
            .select("dl.film-data-list")
            .select("dd")[1].text()
    }

    fun getMayakovskogoDescription(doc: Document): String {
        return doc.getElementsByClass("description-info").text()
    }

    fun getMayakovskogoItemSize(): Int {
        val doc = Jsoup.connect(Constants.MAYAK_ZP).get()
        return doc.getElementsByClass("film-title-list").size
    }

    fun getMayakovskogoTitlePremiere(element: Element): String {
        return element.text()
    }

    fun getMayakovskogoPosterUrlPremiere(element: Element): String {
        return element.outerHtml()
            .replaceBefore("data-src=\"", "")
            .replace("data-src=\"", "")
            .replaceAfter("\" class=\"lazyload\"", "")
            .replace("\" class=\"lazyload\"", "")
    }

    fun getMayakovskogoMovieUrlPremiere(element: Element): String {
        return Constants.MAYAK_BASE_URL + element.select("a").attr("href")
    }

    //*Multiplex

    fun getMultiplexSchedule(doc: Document): MutableList<Session> {
        val listOfSessionsToList = doc.getElementsByClass("as_schedule")[1]
            .select("div.cinema").text()
            .replaceAfter("Сеанси в інших містах", "")
            .replace("Сеанси в інших містах", "")
            .replace(" 3D", "3D")
            .replace("3D,", "3D")
            .replace(" ", ",")
            .replace("3D,", "3D")
            .replace(",", " 2D,")
            //need to remove
            .replace("Аврора 2D,", "")
            //****
            .replace("3D", " 3D,")
            .split(",")

        var count = 0
        val sessionsWithTimeList = mutableListOf<String>()
        val threeDList = mutableListOf<Session>()
        val twoDList = mutableListOf<Session>()
        val resultList = mutableListOf<Session>()

        while (count < listOfSessionsToList.size - 1) {
            val oneSession = doc.getElementsByClass("as_schedule")[1]
                .select("div.cinema")
                .select("div.ns")[count]
                .select("p").text()
                .replace(" ", " 2D, ")
                .replace("2D, 3D", "3D, ")

            val onePrice = doc.getElementsByClass("as_schedule")[1]
                .select("div.cinema")
                .select("div.ns")[count].attr("data-low")

            count += 1
            sessionsWithTimeList.add(oneSession + onePrice)
        }

        for (i in sessionsWithTimeList) {
            if (i.contains("3D")) {
                threeDList += Session(
                    "3D",
                    i.substringBefore(" ") + " від " + (i.substringAfter("3D, ")
                        .toInt() / 100) + "грн"
                )
            }
        }
        for (i in sessionsWithTimeList) {
            if (i.contains("2D")) {
                twoDList += Session(
                    "2D",
                    i.substringBefore(" ") + " від " + (i.substringAfter("2D, ")
                        .toInt() / 100) + "грн"
                )
            }
        }

        var threeDString = ""
        var twoDString = ""

        for (s in threeDList) {
            threeDString += s.time_price + "\n"
        }
        for (s in twoDList) {
            twoDString += s.time_price + "\n"
        }

        if (twoDString.isNotEmpty()) resultList.add(Session("2D", twoDString))
        if (threeDString.isNotEmpty()) resultList.add(Session("3D", threeDString))

        return resultList
    }

    fun getMultiplexAge(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li.rating")
            .select("div.val").text().substring(0, 15)
    }

    fun getMultiplexGenre(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li")[7]
            .select("p.val").text()
    }

    fun getMultiplexCountry(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li")[9]
            .select("p.val").text()
    }

    fun getMultiplexYear(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li")[1]
            .select("p.val").text()
    }

    fun getMultiplexVideoUrl(doc: Document): String {
        return doc.getElementsByClass("only_video_section")
            .select("h2")
            .attr("data-fullyturl")
    }

    fun getMultiplexDescription(doc: Document): String {
        return doc.getElementsByClass("movie_description").text()
    }

    fun getMultiplexItemSize(): Int {
        val doc = Jsoup.connect(Constants.MULTIPLEX_ZP).get()
        val elements = doc.getElementsByClass("cinema_inside sch_date")
        val hashSet = HashSet(
            elements.select("div.film")
                .select("a")
                .eachAttr("title")
        )
        return hashSet.size
    }

    fun getMultiplexTitlePremiere(element: Element): String {
        return element.attr("title")
    }

    fun getMultiplexPosterUrlPremiere(element: Element): String {
        return Constants.MULTIPLEX_BASE_URL + element
            .select("div.poster.is-desktop")
            .toString()
            .replace("<div class=\"poster is-desktop\" style=\"background-image: url('", "")
            .replaceAfter(">", "")
            .replace("')\">", "")
    }

    fun getMultiplexMovieUrlPremiere(element: Element): String {
        return Constants.MULTIPLEX_BASE_URL + element.attr("href")
    }

    //*CinemaCity

    fun getCinemaCitySchedule(doc: Document): MutableList<Session> {
        val listOfSessions = mutableListOf<Session>()
        for (i in getCinemaCityListOfSessions(doc).indices) {
            listOfSessions += Session(
                getCinemaCityListOfSessions(doc)[i],
                getCinemaCityListOfTimes(doc)[i]
            )
        }
        return listOfSessions
    }

    private fun getCinemaCityListOfTimes(doc: Document): List<String> {
        val listOfTimes = mutableListOf<String>()
        for (i in getCinemaCityListOfSessions(doc).indices) {
            val elements = doc.getElementsByClass("session__block")[i]
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
        for (e in elements) {
            listOfSessions += e.select("div.session__type").text()
        }
        return listOfSessions
    }

    fun getCinemaCityGenre(doc: Document): String {
        return doc.getElementsByClass("movie__info")
            .select("div.movie__short-description")
            .select("div.movie__about")
            .select("p")[2].text()
    }

    fun getCinemaCityCountry(doc: Document): String {
        return doc.getElementsByClass("movie__info")
            .select("div.movie__short-description")
            .select("div.movie__about")
            .select("p")[1].text()
    }

    fun getCinemaCityYear(doc: Document): String {
        return doc.getElementsByClass("movie__info")
            .select("div.movie__short-description")
            .select("div.movie__about")
            .select("p")[0].text()
    }

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
        for (e in elements) {
            desc = e.select("div")
                .text()
        }
        return desc
    }

    fun getCinemaCityBackground(doc: Document): String {
        val elements = doc.getElementsByClass("movie-video-container")
        return (Constants.CINEMA_CITY_BASE_URL + elements
            .select("img.movie-video-container__img")
            .attr("src"))
    }

    fun getCinemaCityTitlePremiere(element: Element): String {
        return element.select("img.poster__img")
            .attr("alt")
            .replace("фільм", "")
            .replace("постер", "").trim()
    }

    fun getCinemaCityPosterUrlPremiere(element: Element): String {
        return (Constants.CINEMA_CITY_BASE_URL + element.select("img.poster__img")
            .attr("src"))
    }

    fun getCinemaCityMovieUrlPremiere(element: Element): String {
        return element.select("a")
            .attr("href")
    }

    fun getCinemaCityAgePremiere(element: Element): String {
        return element.select("div.poster__info")
            .select("a.poster__name")
            .select("span.age-limitation")
            .text()
    }

    fun getCinemaCityMovieUrlSoon(element: Element): String {
        return (Constants.CINEMA_CITY_BASE_URL + element.select("a")
            .attr("href"))
    }

    fun getCinemaCityTitleSoon(element: Element): String {
        return element.select("div.on-screen-soon")
            .select("div.on-screen-soon__title")
            .text()
    }

    fun getCinemaCityPosterUrlSoon(element: Element): String {
        return (Constants.CINEMA_CITY_BASE_URL + element.select("div.poster-small")
            .select("img")
            .attr("src"))
    }

    fun getCinemaCityDateSoon(element: Element): String {
        return element.select("div.on-screen-soon")
            .select("div.on-screen-soon__date")
            .text()
    }
}
package com.fara.nearbymovies.repository

import androidx.lifecycle.ViewModel
import com.fara.nearbymovies.entity.Session
import com.fara.nearbymovies.utils.Constants
import com.fara.nearbymovies.utils.Constants.Companion.MULTIPLEX_BASE_URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


class MultiplexRepository : ViewModel() {

    fun getSchedule(doc: Document): MutableList<Session> {
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

    fun getAge(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li.rating")
            .select("div.val").text().substring(0, 15)
    }

    fun getGenre(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li")[7]
            .select("p.val").text()
    }

    fun getCountry(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li")[9]
            .select("p.val").text()
    }

    fun getYear(doc: Document): String {
        return doc.getElementsByClass("movie_credentials")
            .select("li")[1]
            .select("p.val").text()
    }

    fun getVideoUrl(doc: Document): String {
        return doc.getElementsByClass("only_video_section")
            .select("h2")
            .attr("data-fullyturl")
    }

    fun getDescription(doc: Document): String {
        return doc.getElementsByClass("movie_description").text()
    }

    fun getItemSize(): Int {
        val doc = Jsoup.connect(Constants.MULTIPLEX_ZP).get()
        val elements = doc.getElementsByClass("cinema_inside sch_date")
        val hashSet = HashSet(
            elements.select("div.film")
                .select("a")
                .eachAttr("title")
        )
        return hashSet.size
    }

    fun getTitlePremiere(element: Element): String {
        return element.attr("title")
    }

    fun getPosterUrlPremiere(element: Element): String {
        return MULTIPLEX_BASE_URL + element
            .select("div.poster.is-desktop")
            .toString()
            .replace("<div class=\"poster is-desktop\" style=\"background-image: url('", "")
            .replaceAfter(">", "")
            .replace("')\">", "")
    }

    fun getMovieUrlPremiere(element: Element): String {
        return MULTIPLEX_BASE_URL + element.attr("href")
    }
}
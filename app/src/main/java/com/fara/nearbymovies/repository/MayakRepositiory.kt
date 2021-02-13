package com.fara.nearbymovies.repository

import com.fara.nearbymovies.utils.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class MayakRepositiory {

//    fun getSchedule(doc: Document): MutableList<Session> {
//        val listOfSessions = mutableListOf<Session>()
//        for (i in getListOfSessions(doc).indices) {
//            listOfSessions += Session(getListOfSessions(doc)[i], getListOfTimes(doc)[i])
//        }
//        Log.d("tea", listOfSessions.toString())
//        return listOfSessions
//    }
//
//    private fun getListOfTimes(doc: Document): List<String> {
//        val listOfTimes = mutableListOf<String>()
//        for (i in getListOfSessions(doc).indices) {
//            val elements = doc.getElementsByClass("session__block")[i]
//            listOfTimes += elements.select("div.session__schedule").text()
//                //need to delete space only
//                .replace("грн VIP ", "грнVIP\n")
//                //need to be removed if the line is last
//                .replace("грн VIP", "грнVIP")
//                .replace("грн ", "грн\n")
//                .replace("грнVIP", "грн VIP")
//        }
//        return listOfTimes
//    }
//
//    private fun getListOfSessions(doc: Document): List<String> {
//        val elements = doc.getElementsByClass("session__block")
//        val listOfSessions = mutableListOf<String>()
//        for (e in elements) {
//            listOfSessions += e.select("div.session__type").text()
//        }
//        return listOfSessions
//    }

    fun getGenre(doc: Document): String {
        return doc.getElementsByClass("film-block")
            .select("dl.film-data-list")
            .select("dd")[0].text()
    }

    fun getCountry(doc: Document): String {
        return doc.getElementsByClass("aside-info")
            .select("dl.film-data-list")
            .select("dd")[2].text()
    }

    fun getYear(doc: Document): String {
        return doc.getElementsByClass("aside-info")
            .select("dl.film-data-list")
            .select("dd")[1].text()
    }

    fun getDescription(doc: Document): String {
        return doc.getElementsByClass("description-info").text()
    }

    fun getItemSize(): Int {
        val doc = Jsoup.connect(Constants.MAYAK_ZP).get()
        return doc.getElementsByClass("film-title-list").size
    }

    fun getTitlePremiere(element: Element): String {
        return element.text()
    }

    fun getPosterUrlPremiere(element: Element): String {
        return element.outerHtml()
            .replaceBefore("data-src=\"", "")
            .replace("data-src=\"", "")
            .replaceAfter("\" class=\"lazyload\"", "")
            .replace("\" class=\"lazyload\"", "")
    }

    fun getMovieUrlPremiere(element: Element): String {
        return Constants.MAYAK_BASE_URL + element.select("a").attr("href")
    }
}
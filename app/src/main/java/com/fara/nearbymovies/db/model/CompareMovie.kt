package com.fara.nearbymovies.db.model

import com.fara.nearbymovies.entity.Session

data class CompareMovie(
    val city: String = "",
    val cinema: String = "",
    val session: List<Session> = emptyList()
)
package com.fara.nearbymovies.entity

import java.io.Serializable

data class Soon(
    val title: String,
    val poster_url: String,
    val date: String,
    val movie_url: String,
) : Serializable
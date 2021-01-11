package com.fara.nearbymovies.entity

import java.io.Serializable

data class Premiere(
    val title: String,
    val poster_url: String,
    val movie_url: String,
    val age: String,
) : Serializable
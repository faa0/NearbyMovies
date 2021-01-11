package com.fara.nearbymovies.entity

import java.io.Serializable

data class Detail(
    val background: String,
    val description: String,
    val video_url: String,
    val year: String,
    val country: String,
    val genre: String,
    val schedule: List<Session>?,
) : Serializable

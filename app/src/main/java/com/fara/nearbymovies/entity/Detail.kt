package com.fara.nearbymovies.entity

import java.io.Serializable

data class Detail(
    val background: String? = null,
    val description: String? = null,
    val video_url: String? = null,
    val year: String? = null,
    val country: String? = null,
    val genre: String? = null,
    val schedule: List<Session>?,
) : Serializable

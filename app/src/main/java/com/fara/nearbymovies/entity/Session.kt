package com.fara.nearbymovies.entity

import java.io.Serializable

data class Session(
    val session: String,
    val time_price: String,
) : Serializable

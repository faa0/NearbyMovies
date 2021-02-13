package com.fara.nearbymovies.entity

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "cinema_table", primaryKeys = ["title", "cinema", "city"])
data class Cinema(
    val cinema: String = "",
    val city: String = "",
    val title: String = "",
    val poster_url: String? = null,
    val movie_url: String? = null,
    val age: String? = null
) : Serializable
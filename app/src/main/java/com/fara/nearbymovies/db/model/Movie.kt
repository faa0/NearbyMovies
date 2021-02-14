package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.fara.nearbymovies.db.Converter
import com.fara.nearbymovies.entity.Session

@Entity(tableName = "movies_table", primaryKeys = ["title", "cinema", "city"])
data class Movie(
    val city: String = "",
    val cinema: String = "",
    val title: String = "",
    @TypeConverters(Converter::class)
    val session: List<Session>?,
)

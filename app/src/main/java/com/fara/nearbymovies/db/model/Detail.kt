package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import retrofit2.Converter

@Entity(
    tableName = "detail",
    foreignKeys = [ForeignKey(
        entity = Preview::class,
        parentColumns = ["movie_url"],
        childColumns = ["movie_url"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Detail(
    @PrimaryKey
    val movie_url: String,
    val cinema_id: Long = 0,
    val description: String,
    val year: String,
    val country: String,
    val genre: String,
    val background: String? = null,
    val video_url: String? = null,
    @TypeConverters(Converter::class)
    val session: List<Session> = emptyList(),
)
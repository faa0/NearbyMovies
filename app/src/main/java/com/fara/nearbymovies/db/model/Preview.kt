package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "preview",
    foreignKeys = [ForeignKey(
        entity = Cinema::class,
        parentColumns = ["id"],
        childColumns = ["cinema_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["cinema_id"]),
        Index(value = ["movie_url"], unique = true)
    ]
)
data class Preview(
    val cinema_id: Long = 0,
    val title: String,
    val poster_url: String,
    @PrimaryKey
    val movie_url: String,
    val date: String? = null,
    val age: String? = null,
    val soon: Boolean
) : Serializable
package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
        Index(value = ["title"], unique = true)
    ]
)
data class Preview(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cinema_id: Long,
    val title: String,
    val poster_url: String,
    val movie_url: String,
    val date: String? = null,
    val age: String? = null
)
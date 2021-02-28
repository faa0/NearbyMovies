package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cinema",
    foreignKeys = [ForeignKey(
        entity = City::class,
        parentColumns = ["id"],
        childColumns = ["city_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["city_id"]),
        Index(value = ["cinema"], unique = true)
    ]
)
data class Cinema(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city_id: Long,
    val cinema: String
)
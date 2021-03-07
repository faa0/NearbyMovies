package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cinema",
    foreignKeys = [ForeignKey(
        entity = City::class,
        parentColumns = ["id"],
        childColumns = ["city_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Cinema(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val city_id: Long,
    val cinema: String
)
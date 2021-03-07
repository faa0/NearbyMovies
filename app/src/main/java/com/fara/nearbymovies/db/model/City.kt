package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val city: String
)
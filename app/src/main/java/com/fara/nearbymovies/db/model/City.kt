package com.fara.nearbymovies.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "city",
    indices = [Index(value = ["city"], unique = true)]
)
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String
)
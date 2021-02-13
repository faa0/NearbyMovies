package com.fara.nearbymovies.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fara.nearbymovies.db.Converter
import com.fara.nearbymovies.entity.Session

@Entity(
    tableName = "movies_table",
//    foreignKeys = [ForeignKey(
//        entity = Cinema::class,
//        parentColumns = ["title"],
//        childColumns = ["cinema_id"],
//        onDelete = CASCADE
//    )]
)
data class Movie(
    val background: String? = null,
    val description: String? = null,
    val video_url: String? = null,
    val year: String? = null,
    val country: String? = null,
    val genre: String? = null,
    @TypeConverters(Converter::class)
    val session: List<Session>?,
    @PrimaryKey
    @ColumnInfo(name = "cinema_id")
    val cinemaId: Long = 0
)

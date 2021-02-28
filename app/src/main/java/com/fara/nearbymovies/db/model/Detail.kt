package com.fara.nearbymovies.db.model

import androidx.room.*
import retrofit2.Converter

@Entity(
    tableName = "detail",
    foreignKeys = [ForeignKey(
        entity = Preview::class,
        parentColumns = ["id"],
        childColumns = ["preview_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["preview_id"])]
)
data class Detail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val preview_id: Long,
    val description: String,
    val year: String,
    val country: String,
    val genre: String,
    val background: String? = null,
    val video_url: String? = null,
    @TypeConverters(Converter::class)
    val session: List<Session> = emptyList(),
)
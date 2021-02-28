package com.fara.nearbymovies.db

import androidx.room.TypeConverter
import com.fara.nearbymovies.db.model.Session
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    @TypeConverter
    fun toString(value: List<Session>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Session>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toList(value: String): List<Session> {
        val gson = Gson()
        val type = object : TypeToken<List<Session>>() {}.type
        return gson.fromJson(value, type)
    }
}
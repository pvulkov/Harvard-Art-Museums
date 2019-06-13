package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Poster


class PosterConverter {

    @TypeConverter
    fun fromString(value: String?): Poster? {
        val type = object : TypeToken<Poster>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromData(info: Poster?): String? {
        return Gson().toJson(info)
    }

}
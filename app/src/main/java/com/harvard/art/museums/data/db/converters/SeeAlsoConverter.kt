package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.SeeAlso


class SeeAlsoConverter {

    @TypeConverter
    fun fromString(value: String): List<SeeAlso> {
        val type = object : TypeToken<List<SeeAlso>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromData(list: List<SeeAlso>): String {
        return Gson().toJson(list)
    }

}
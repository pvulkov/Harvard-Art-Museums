package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Details


class DetailsConverter {

    @TypeConverter
    fun fromString(value: String?): Details? {
        val type = object : TypeToken<Details>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromData(info: Details?): String? {
        return Gson().toJson(info)
    }

}
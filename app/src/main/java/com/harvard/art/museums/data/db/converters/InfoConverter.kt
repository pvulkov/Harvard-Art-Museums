package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Info
import com.harvard.art.museums.data.pojo.Record


class InfoConverter {

    @TypeConverter
    fun fromString(value: String?): Info? {
        val type = object : TypeToken<Info>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromPhotoData(info: Info?): String? {
        return Gson().toJson(info)
    }

}
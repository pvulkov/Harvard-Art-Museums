package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Image


class ImageConverter {

    @TypeConverter
    fun fromString(value: String): List<Image> {
        val listType = object : TypeToken<List<Image>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Image>): String {
        val jsonString = Gson().toJson(list)
        return jsonString
    }

}
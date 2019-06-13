package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.People


class PeopleConverter {

    @TypeConverter
    fun fromString(value: String): List<People> {
        val listType = object : TypeToken<List<People>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<People>): String {
        val jsonString = Gson().toJson(list)
        return jsonString
    }

}
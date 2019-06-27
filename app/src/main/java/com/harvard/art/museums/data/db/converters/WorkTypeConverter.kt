package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Worktype


class WorkTypeConverter {

    @TypeConverter
    fun fromString(value: String): List<Worktype> {
        val type = object : TypeToken<List<Worktype>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromData(info: List<Worktype>): String {
        return Gson().toJson(info)
    }

}
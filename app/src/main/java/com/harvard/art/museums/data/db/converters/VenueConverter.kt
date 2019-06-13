package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Venue


class VenueConverter {

    @TypeConverter
    fun fromString(value: String): List<Venue> {
        val listType = object : TypeToken<List<Venue>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Venue>): String {
        val jsonString = Gson().toJson(list)
        return jsonString
    }

}
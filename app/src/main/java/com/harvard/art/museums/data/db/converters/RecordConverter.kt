package com.harvard.art.museums.data.db.converters


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harvard.art.museums.data.pojo.Record


class RecordConverter {

    @TypeConverter
    fun fromString(value: String): List<Record> {
        val listType = object : TypeToken<List<Record>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Record>): String {
        val jsonString = Gson().toJson(list)
        return jsonString
    }

}
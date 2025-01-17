package com.harvard.art.museums.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people_data_table")
data class People(
        @PrimaryKey(autoGenerate = true)
        val alphasort: String,
        val birthplace: String,
        val culture: String,
        val deathplace: String,
        val displaydate: String,
        val displayname: String,
        val displayorder: Int,
        val gender: String,
        val name: String,
        val personid: Int,
        val prefix: String,
        val role: String
)
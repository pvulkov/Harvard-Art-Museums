package com.harvard.art.museums.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people_data_table")
data class People(
        @PrimaryKey(autoGenerate = true)
        val rid: Long,
        val displayname: String,
        val displayorder: Int,
        val name: String,
        val personid: Int,
        val prefix: String,
        val role: String
)
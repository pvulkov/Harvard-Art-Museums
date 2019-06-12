package com.harvard.art.museums.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exhibitions_data_table")
data class Exhibitions(

        @PrimaryKey(autoGenerate = true)
        val rid: Long,
        val info: Info,
        val records: List<Record>
)

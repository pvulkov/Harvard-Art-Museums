package com.harvard.art.museums.data.pojo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exhibition_records_data_table")
data class ExhibitionRecord(

        @PrimaryKey
        val id: Int,
        val begindate: String,
        val color: String? = null,
        val description: String? = null,
        val textiledescription: String? = null,
        val enddate: String,
        val exhibitionid: Int,
        val images: List<Image>? = null,
        val url: String,
        val lastupdate: String? = null,
        val poster: Poster? = null,
        val primaryimageurl: String? = null,
        val shortdescription: String? = null,
        val temporalorder: Int,
        val title: String,
        val venues: List<Venue>? = null,
        val people: List<People>? = null,
        @Embedded
        val info: Info
)

package com.harvard.art.museums.data.pojo

data class Record(
        val begindate: String,
        val color: Any,
        val description: Any,
        val enddate: String,
        val exhibitionid: Int,
        val id: Int,
        val images: List<Image>,
        val lastupdate: String,
        val poster: Poster,
        val primaryimageurl: String,
        val shortdescription: Any,
        val temporalorder: Int,
        val title: String,
        val venues: List<Venue>
)
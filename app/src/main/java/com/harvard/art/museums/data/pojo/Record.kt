package com.harvard.art.museums.data.pojo

data class Record(
        val begindate: String,
        val color: String? = null,
        val description: String,
        val enddate: String,
        val exhibitionid: Int,
        val textiledescription: String? = null,
        val id: Int,
        val images: List<Image>? = null,
        val url: String,
        val lastupdate: String,
        val poster: Poster,
        val primaryimageurl: String? = null,
        val shortdescription: String,
        val temporalorder: Int,
        val title: String,
        val venues: List<Venue>? = null,
        val people: List<People>? = null
)
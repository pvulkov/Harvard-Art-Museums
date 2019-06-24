package com.harvard.art.museums.data.pojo

data class Exhibition(
    val begindate: String,
    val color: String,
    val description: String,
    val enddate: String,
    val exhibitionid: Int,
    val id: Int,
    val images: List<Image>? = null,
    val lastupdate: String,
    val poster: Poster,
    val primaryimageurl: String,
    val shortdescription: String,
    val temporalorder: Int,
    val textiledescription: String,
    val title: String,
    val url: String,
    val venues: List<Venue>
)
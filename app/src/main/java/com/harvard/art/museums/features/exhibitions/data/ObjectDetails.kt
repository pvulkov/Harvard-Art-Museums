package com.harvard.art.museums.features.exhibitions.data

import com.harvard.art.museums.data.pojo.Image


data class ObjectDetails(
        val info: Info,
        val records: List<Record>
)

data class Info(
        val page: Int,
        val pages: Int,
        val totalrecords: Int,
        val totalrecordsperquery: Int
)


data class Record(
        val accessionmethod: String,
        val accessionyear: Int,
        val accesslevel: Int,
        val century: String,
        val classification: String,
        val classificationid: Int,
        val colorcount: Int,
        val colors: List<Color>,
        val commentary: Any,
        val contact: String,
        val contextualtextcount: Int,
        val copyright: Any,
        val creditline: String,
        val culture: String,
        val datebegin: Int,
        val dated: String,
        val dateend: Int,
        val dateoffirstpageview: String,
        val dateoflastpageview: String,
        val department: String,
        val description: Any,
        val dimensions: String,
        val division: String,
        val edition: Any,
        val exhibitioncount: Int,
        val groupcount: Int,
        val id: Int,
        val imagecount: Int,
        val imagepermissionlevel: Int,
        val images: List<Image>? = null,
        val labeltext: Any,
        val lastupdate: String,
        val markscount: Int,
        val mediacount: Int,
        val medium: String,
        val objectid: Int,
        val objectnumber: String,
        val people: List<People>,
        val peoplecount: Int,
        val period: Any,
        val periodid: Any,
        val primaryimageurl: String,
        val provenance: String,
        val publicationcount: Int,
        val rank: Int,
        val relatedcount: Int,
        val seeAlso: List<SeeAlso>,
        val signed: Any,
        val standardreferencenumber: Any,
        val state: Any,
        val style: Any,
        val technique: Any,
        val techniqueid: Any,
        val title: String,
        val titlescount: Int,
        val totalpageviews: Int,
        val totaluniquepageviews: Int,
        val url: String,
        val verificationlevel: Int,
        val verificationleveldescription: String,
        val worktypes: List<Worktype>
)

data class Color(
        val color: String,
        val css3: String,
        val hue: String,
        val percent: Double,
        val spectrum: String
)


data class Worktype(
        val worktype: String,
        val worktypeid: String
)

data class People(
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
        val prefix: Any,
        val role: String
)

data class SeeAlso(
        val format: String,
        val id: String,
        val profile: String,
        val type: String
)
package com.harvard.art.museums.data.pojo

import com.harvard.art.museums.ext.EMPTY

data class Image(
        val baseimageurl: String,
        val caption: String,
        val copyright: String? = EMPTY,
        val displayorder: Int? = 0,
        val renditionnumber: String? = EMPTY,
        val format: String? = EMPTY,
        val height: Int? = 0,
        val idsid: Int? = 0,
        val iiifbaseuri: String? = EMPTY,
        val imageid: Int? = 0,
        val publiccaption: String? = EMPTY,
        val width: Int? = 0
)


package com.harvard.art.museums.features.exhibitions.data

import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.data.pojo.Poster


data class GalleryObjectData(
        val title: String,
        val images: List<Image> = listOf(),
        val poster: Poster? = null,
        val description: String? = null,
        val dateFromTo: String? = null,
        val location: String? = null
)

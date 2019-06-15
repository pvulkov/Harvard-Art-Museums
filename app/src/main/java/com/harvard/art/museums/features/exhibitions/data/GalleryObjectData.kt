package com.harvard.art.museums.features.exhibitions.data

import com.harvard.art.museums.data.pojo.Image


data class GalleryObjectData(
        val images: List<Image> = listOf(),
        val description: String? = null
)

package com.harvard.art.museums.features.exhibitions

import com.harvard.art.museums.ext.EMPTY

data class ExhibitionViewItem(
        val type: ViewItemType = ViewItemType.DATA,
        val title: String? = EMPTY,
        val imageUrl: String? = null,
        val exhibitionFromTo: String? = EMPTY,
        val next: String? = null

)

enum class ViewItemType {
    DATA, LOADER
}
package com.harvard.art.museums.features.objects

import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.features.exhibitions.data.ViewItemType

data class ObjectViewItem(
        val objectId: Int,
        val viewType: ViewItemType,
        val text: String,
        val image: Image?,
        val nextUrl: String = EMPTY,
        var span: Int = 2
)


enum class ViewItemType {
    DATA, LOADER
}
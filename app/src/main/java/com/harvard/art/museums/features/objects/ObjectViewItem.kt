package com.harvard.art.museums.features.objects

import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.features.exhibitions.data.ViewItemType

class ObjectViewItem(
        viewType: ViewType,
        val objectId: Int = -1,
        val text: String? = EMPTY,
        val image: Image? = null,
        val nextUrl: String = EMPTY
) : ViewItemType(viewType)


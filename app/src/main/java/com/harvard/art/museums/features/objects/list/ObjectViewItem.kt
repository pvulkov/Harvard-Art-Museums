package com.harvard.art.museums.features.objects.list

import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.features.exhibitions.data.ViewItemType

class ObjectViewItem(
        viewType: ViewType,
        val id: Int = -1,
        val text: String? = EMPTY,
        val objectId: Int = -1,
        val objectNumber: String? = EMPTY,
        val objectAuthor: String? = EMPTY,
        val objectCategory: String? = EMPTY,
        val image: Image? = null,
        val nextUrl: String = EMPTY
) : ViewItemType(viewType)


package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.Record
import com.harvard.art.museums.features.exhibitions.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.ViewItemType


fun Record.toExhibitionViewItem(type: ViewItemType = ViewItemType.DATA): ExhibitionViewItem {

    return ExhibitionViewItem(
            type,
            title,
            primaryimageurl,
            begindate + " " + enddate
    )
}
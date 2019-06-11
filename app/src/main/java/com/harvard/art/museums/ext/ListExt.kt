package com.harvard.art.museums.ext

import com.harvard.art.museums.features.exhibitions.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.ViewItemType


fun <T> MutableList<T>.setData(data: List<T>) {
    this.clear()
    this.addAll(data)
}


fun List<ExhibitionViewItem>.trimLoaders() : MutableList<ExhibitionViewItem> {
    return this.filter { it.type == ViewItemType.DATA }.toMutableList()
}
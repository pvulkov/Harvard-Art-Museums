package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.ObjectDetails
import com.harvard.art.museums.features.search.SearchResultViewItem
import com.harvard.art.museums.features.search.SearchResultViewType


fun ObjectDetails.toSearchViewItems(): List<SearchResultViewItem> {

    val result = mutableListOf<SearchResultViewItem>()

    this.records.forEach {
        val item = SearchResultViewItem(
                SearchResultViewType.OBJECT,
                it.id,
                it.title ?: EMPTY,
                it.primaryimageurl,
                1)
        result.add(item)
    }


    return result
}
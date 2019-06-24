package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.RecentSearchRecord
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewItemType
import com.harvard.art.museums.features.search.SearchResultViewItem
import com.harvard.art.museums.features.search.SearchResultViewType

fun <T> MutableList<T>.setData(data: List<T>) {
    this.clear()
    this.addAll(data)
}


fun List<ExhibitionViewItem>.update(newList: List<ExhibitionViewItem>): List<ExhibitionViewItem> {


    val filteredList = newList.filter { !this.contains(it) }.toList()

    return this.plus(filteredList)
}

fun List<ExhibitionViewItem>.trimLoaders(): MutableList<ExhibitionViewItem> {
    return this.filter { it.type == ViewItemType.DATA }.toMutableList()
}

fun List<RecentSearchRecord>.toSearchViewItems(): List<SearchResultViewItem> {
    return this.map {
        SearchResultViewItem(
                SearchResultViewType.values()[it.viewType],
                it.objectId ?: -1,
                it.text,
                it.imageUrl,
                2)
    }.toList()
}
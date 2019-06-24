package com.harvard.art.museums.features.search

data class SearchResultViewItem(
        var viewType: SearchResultViewType,
        val objectId: Int,
        val text: String,
        val imageUrl: String? = null,
        val span: Int = 2
)

enum class SearchResultViewType {
    EXHIBITION, OBJECT, RECENT_SEARCH, RECENT_EXHIBITION, RECENT_OBJECT
}
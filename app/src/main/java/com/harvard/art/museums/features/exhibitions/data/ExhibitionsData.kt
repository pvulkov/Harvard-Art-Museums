package com.harvard.art.museums.features.exhibitions.data

import com.harvard.art.museums.ext.EMPTY

data class ExhibitionViewItem(
        val type: ViewItemType = ViewItemType.DATA,
        val title: String? = EMPTY,
        val imageUrl: String = EMPTY,
        val exhibitionUrl: String? = EMPTY,
        val exhibitionFromTo: String? = EMPTY,
        val people: String? = EMPTY,
        val next: String? = null

)


fun ExhibitionViewItem.toViewItemAction(action: ViewAction) = ViewItemAction(action, this)

enum class ViewItemType {
    DATA, LOADER
}


data class ViewItemAction(
        val action: ViewAction = ViewAction.NONE,
        val item: ExhibitionViewItem
)

enum class ViewAction {
    NONE, LOAD_MORE, SHARE, WEB, DETAILS
}
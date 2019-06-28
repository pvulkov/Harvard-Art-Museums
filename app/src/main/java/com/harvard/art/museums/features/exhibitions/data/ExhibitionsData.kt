package com.harvard.art.museums.features.exhibitions.data

import com.harvard.art.museums.ext.EMPTY

class ExhibitionViewItem(
        viewType: ViewType = ViewType.DATA,
        val title: String? = EMPTY,
        val exhibitionId: Int,
        val imageUrl: String = EMPTY,
        val shortDescription: String? = null,
        val openStatus: Int = -1,
        val exhibitionUrl: String? = EMPTY,
        val exhibitionFromTo: String? = EMPTY,
        val exhibitionLocation: String? = null,
        val people: String? = EMPTY,
        val next: String? = null

) : ViewItemType(viewType)

//TODO (pvalkov) ViewItemType is now a super for objects and exhibitions
// place into a different folder/file

open class ViewItemType(val viewType: ViewType) {
    enum class ViewType { DATA, LOADER }
}


fun ExhibitionViewItem.toViewItemAction(action: ViewAction) = ViewItemAction(action, this)


data class ViewItemAction(
        val action: ViewAction = ViewAction.NONE,
        val item: ExhibitionViewItem
)

enum class ViewAction {
    NONE, LOAD_MORE, SHARE, WEB, DETAILS
}

enum class ExhibitionOpenStatus {
    OPEN, UPCOMING, CLOSED
}
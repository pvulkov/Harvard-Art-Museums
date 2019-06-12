package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.Record
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewItemType


fun Record.toExhibitionViewItem(type: ViewItemType = ViewItemType.DATA): ExhibitionViewItem {

    return ExhibitionViewItem(
            type,
            title,
            //TODO (pvalkov) provide default image url
            primaryimageurl ?: EMPTY,
            url,
            formatFromToDate(),
            getPeople()
    )
}


private fun Record.formatFromToDate(): String {

    val from = begindate.fromServerDate()
    val to = enddate.fromServerDate()


    return "${from.to_ddMMMMyyyy()} - ${to.to_ddMMMMyyyy()}"
}

private fun Record.getPeople(): String {

    if (people.isNullOrEmpty())
        return EMPTY

    var peopleStr = EMPTY
    val rolesMap = people.groupBy { it.prefix }
    for ((t, u) in rolesMap) {
        peopleStr += u.joinToString(", ", "$t ") { it.name }
    }

    return peopleStr
}
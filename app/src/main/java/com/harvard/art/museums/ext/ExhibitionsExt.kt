package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewItemType


fun Exhibitions.toExhibitionRecordsList(nextUrl: String): List<ExhibitionRecord> {

    val info = this.info
    info.cur = nextUrl
    return this.records.map { it.toExhibitionRecord(info) }
}




fun ExhibitionRecord.toExhibitionViewItem(type: ViewItemType = ViewItemType.DATA): ExhibitionViewItem {

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

private fun ExhibitionRecord.formatFromToDate(): String {

    val from = begindate.fromServerDate()
    val to = enddate.fromServerDate()


    return "${from.to_ddMMMMyyyy()} - ${to.to_ddMMMMyyyy()}"
}

private fun ExhibitionRecord.getPeople(): String {

    if (people.isNullOrEmpty())
        return EMPTY

    var peopleStr = EMPTY
    val rolesMap = people.groupBy { it.prefix }
    for ((t, u) in rolesMap) {
        peopleStr += u.joinToString(", ", "$t ") { it.name }
    }

    return peopleStr
}

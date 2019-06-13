package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.*
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


fun Record.toExhibitionRecord(info: Info): ExhibitionRecord {

    return ExhibitionRecord(
            id = this.id,
            begindate = begindate,
            color = color,
            description = description,
            enddate = enddate,
            exhibitionid = exhibitionid,
            images = images?: listOf(),
            url = url,
            lastupdate = lastupdate,
            poster = poster,
            primaryimageurl = primaryimageurl,
            shortdescription = shortdescription,
            temporalorder = temporalorder,
            title = title,
            venues = venues?: listOf(),
            people = people?: listOf(),
            info = info
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
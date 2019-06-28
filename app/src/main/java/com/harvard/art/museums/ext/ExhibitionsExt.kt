package com.harvard.art.museums.ext

import com.harvard.art.museums.R
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.data.pojo.Info
import com.harvard.art.museums.data.pojo.Record
import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem
import com.harvard.art.museums.features.exhibitions.data.ExhibitionOpenStatus
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem
import com.harvard.art.museums.features.search.SearchResultViewItem
import com.harvard.art.museums.features.search.SearchResultViewType
import com.harvard.art.museums.features.exhibitions.data.ViewItemType.ViewType




fun Exhibitions.toExhibitionRecordsList(nextUrl: String): List<ExhibitionRecord> {

    val info = this.info
    info.cur = nextUrl
    return this.records.map { it.toExhibitionRecord(info) }
}


fun ExhibitionRecord.toExhibitionViewItem(viewType: ViewType = ViewType.DATA): ExhibitionViewItem {

    return ExhibitionViewItem(
            viewType,
            title,
            id,
            //TODO (pvalkov) provide default image url
            primaryimageurl ?: EMPTY,
            description,
            getOpenStatusResId(),
            url,
            formatFromToDate(),
            formatLocation(),
            getPeople()
    )
}

fun ExhibitionRecord.toExhibitionDetailsViewItem(viewType: ViewType = ViewType.DATA): ExhibitionDetailsViewItem {
    return ExhibitionDetailsViewItem(
            viewType,
            title,
            id,
            //TODO (pvalkov) provide default image url
            primaryimageurl ?: EMPTY,
            url,
            textiledescription,
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
            textiledescription = textiledescription,
            images = images ?: listOf(),
            url = url,
            lastupdate = lastupdate,
            poster = poster,
            primaryimageurl = primaryimageurl,
            shortdescription = shortdescription,
            temporalorder = temporalorder,
            title = title ?: EMPTY,
            venues = venues ?: listOf(),
            people = people ?: listOf(),
            info = info,
            openStatus = getOpenStatus()
    )
}


fun Exhibitions.toSearchViewItems(): List<SearchResultViewItem> {

    return this.records.map {
        SearchResultViewItem(
                SearchResultViewType.EXHIBITION,
                it.id,
                it.title ?: EMPTY,
                it.poster?.imageurl ?: EMPTY,
                2)
    }.toList()

}


private fun Record.getOpenStatus(): Int {

    if (enddate.fromServerDate().hasPassed())
        return ExhibitionOpenStatus.CLOSED.ordinal

    if (!begindate.fromServerDate().hasPassed())
        return ExhibitionOpenStatus.UPCOMING.ordinal

    return ExhibitionOpenStatus.OPEN.ordinal
}


private fun ExhibitionRecord.getOpenStatus(): ExhibitionOpenStatus {

    if (enddate.fromServerDate().hasPassed())
        return ExhibitionOpenStatus.CLOSED

    if (!begindate.fromServerDate().hasPassed())
        return ExhibitionOpenStatus.UPCOMING

    return ExhibitionOpenStatus.OPEN
}

private fun ExhibitionRecord.getOpenStatusResId(): Int {

    return when (getOpenStatus()) {
        ExhibitionOpenStatus.CLOSED -> R.drawable.exhibition_closed_256
        ExhibitionOpenStatus.OPEN -> R.drawable.exhibition_open_256
        ExhibitionOpenStatus.UPCOMING -> R.drawable.exhibition_upcoming_256
    }
}


fun ExhibitionRecord.formatLocation(): String {
    var addressText = EMPTY
    venues?.apply {


        val addressMap = this.groupBy { it.fullname }
        for ((k, v) in addressMap) {
            when (v[0].galleries.isNullOrEmpty()) {
                false -> v[0].galleries.map { g -> g.name }.joinToString(", ", EMPTY, ", $k")
                else -> ", $k"

            }.also {
                addressText += it
            }

        }

    }

    return addressText
}

fun ExhibitionRecord.formatFromToDate(): String {

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

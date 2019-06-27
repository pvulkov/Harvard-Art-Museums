package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.ObjectRecord
import com.harvard.art.museums.data.pojo.RecordsInfoData
import com.harvard.art.museums.features.objects.ObjectViewItem
import com.harvard.art.museums.features.search.SearchResultViewItem
import com.harvard.art.museums.features.search.SearchResultViewType

fun RecordsInfoData.toObjectViewItems(url: String): List<ObjectViewItem> {

    return listOf()
}

fun RecordsInfoData.toObjectRecordItems(url: String): List<ObjectRecord> {
    val info = this.info
    return this.records.map {
        ObjectRecord(
                it.id,
                it.accessionmethod,
                it.accessionyear,
                it.accesslevel,
                it.century,
                it.classification,
                it.classificationid,
                it.colorcount,
                it.commentary,
                it.contact,
                it.contextualtextcount,
                it.copyright,
                it.creditline,
                it.culture,
                it.datebegin,
                it.dated,
                it.dateend,
                it.dateoffirstpageview,
                it.dateoflastpageview,
                it.department,
                it.description,
                it.details,
                it.dimensions,
                it.division,
                it.edition,
                it.exhibitioncount,
                it.groupcount,
                it.imagecount,
                it.imagepermissionlevel,
                it.images ?: listOf(),
                it.labeltext,
                it.lastupdate,
                it.markscount,
                it.mediacount,
                it.medium,
                it.objectid,
                it.objectnumber,
                it.people ?: listOf(),
                it.peoplecount,
                it.period,
                it.periodid,
                it.primaryimageurl,
                it.provenance,
                it.publicationcount,
                it.rank,
                it.relatedcount,
                it.seeAlso?: listOf(),
                it.signed,
                it.standardreferencenumber,
                it.state,
                it.style,
                it.technique,
                it.techniqueid,
                it.title ?: EMPTY,
                it.titlescount,
                it.totalpageviews,
                it.totaluniquepageviews,
                it.url,
                it.verificationlevel,
                it.verificationleveldescription,
                it.worktypes,
                info,
                url
        )
    }.toList()
}


fun RecordsInfoData.toSearchViewItems(): List<SearchResultViewItem> {

    return this.records.map {
        SearchResultViewItem(
                SearchResultViewType.OBJECT,
                it.id,
                it.title ?: EMPTY,
                it.primaryimageurl,
                1)
    }.toList()
}
package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.*
import com.harvard.art.museums.features.objects.details.*

fun ObjectDetails.toObjectDetailsViewItem(): ObjectDetailsViewItem {
    return ObjectDetailsViewItem(
            this.objectid,
            this.images.firstOrNull()?.baseimageurl,
            this.images.firstOrNull()?.publiccaption,
            this.images,
            generateViewItems()
    )
}

private fun ObjectDetails.generateViewItems(): List<ObjectViewItem> {

    val items = mutableListOf<ObjectViewItem>()

    //#1 add Gallery Text section. !! is safe here
    this.labeltext.ifExists { items.add(GalleryTextViewItem(this.labeltext!!)) }

    //#2  add Identification and Creation section.
    items.add(this.createIdentificationViewItem())


    //TODO (pvalkov) see below
    //#3  add Location

    //#4  add Physical Descriptions View Item
//    items.add(this.createPhysicalDescriptionsViewItem())

    //#5 add  Provenance View Item
//    items.add(this.createProvenanceViewItem())

    return items
}


private fun ObjectDetails.createIdentificationViewItem(): ObjectViewItem =
        IdentificationViewItem(
                this.objectnumber,
                this.title,
                //TODO (pvalkov) see below
                "TODO",
                this.classification,
                getWorkTypes(),
                this.dated,
                getPlaces(),
                this.period,
                this.culture
        )

private fun ObjectDetails.getWorkTypes(): String {
    return this.worktypes.joinToString(",") { it.worktype }
}


private fun ObjectDetails.getPlaces(): String {
    return this.places.joinToString(",") { it.displayname }
}

private fun ObjectDetails.createPhysicalDescriptionsViewItem(): ObjectViewItem =
        PhysicalDescriptionsViewItem(this.medium, this.dimensions)


private fun ObjectDetails.createProvenanceViewItem(): ObjectViewItem =
        ProvenanceViewItem(this.provenance)
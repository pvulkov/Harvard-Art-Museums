package com.harvard.art.museums.features.objects.details

import com.harvard.art.museums.data.pojo.Image


data class ObjectDetailsViewItem(
        val objectId: Int,
        val posterUrl: String? = null,
        val posterCaption: String? = null,
        val images: List<Image>,
        val objectViewItems: List<ObjectViewItem>
)


sealed class ObjectViewItem

data class GalleryTextViewItem(val text: String) : ObjectViewItem()

data class IdentificationViewItem(
        val objectNumber: String,
        val title: String,
        val otherTitles: String? = null,
        val classification: String,
        val worktypes: String,
        val dated: String,
        val places: String,
        val period: String? = null,
        val culture: String
) : ObjectViewItem()


data class LocationViewItem(val location: String) : ObjectViewItem()

data class PhysicalDescriptionsViewItem(
        val medium: String,
        val dimensions: String
) : ObjectViewItem()


data class ProvenanceViewItem(val text: String) : ObjectViewItem()


data class AcquisitionAndRightsViewItem(
        val creditLine: String,
        val accessionYear: Int,
        val objectNumber: String,
        val division: String,
        val contact: String
) : ObjectViewItem()


data class PublicationHistoryViewItem(val text: String) : ObjectViewItem()


data class ExhibitionHistoryViewItem(val text: String) : ObjectViewItem()

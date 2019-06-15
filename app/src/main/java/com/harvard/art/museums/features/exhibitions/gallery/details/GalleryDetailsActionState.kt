package com.harvard.art.museums.features.exhibitions.gallery.details

import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData

sealed class GalleryDetailsActionState {
    object LoadingState : GalleryDetailsActionState()
    data class ErrorState(val error: Throwable) : GalleryDetailsActionState()
    data class DataState(val data: GalleryObjectData) : GalleryDetailsActionState()
}
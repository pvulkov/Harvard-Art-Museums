package com.harvard.art.museums.features.objects.details

import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData

sealed class ObjectDetailsActionState {
    object LoadingState : ObjectDetailsActionState()
    data class ErrorState(val error: Throwable) : ObjectDetailsActionState()
    data class DataState(val data: GalleryObjectData) : ObjectDetailsActionState()
}
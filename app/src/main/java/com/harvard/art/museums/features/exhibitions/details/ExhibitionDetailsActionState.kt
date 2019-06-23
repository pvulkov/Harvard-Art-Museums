package com.harvard.art.museums.features.exhibitions.details

import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData

sealed class ExhibitionDetailsActionState {
    object LoadingState : ExhibitionDetailsActionState()
    data class ErrorState(val error: Throwable) : ExhibitionDetailsActionState()
    data class DataState(val data: GalleryObjectData) : ExhibitionDetailsActionState()
}
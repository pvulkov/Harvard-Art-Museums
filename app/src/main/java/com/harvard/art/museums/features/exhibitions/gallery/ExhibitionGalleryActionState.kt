package com.harvard.art.museums.features.exhibitions.gallery

import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem

sealed class ExhibitionGalleryActionState {
    object LoadingState : ExhibitionGalleryActionState()
    data class ErrorState(val error: Throwable) : ExhibitionGalleryActionState()
    data class DataState(val exhibitionsList: List<ExhibitionDetailsViewItem>) : ExhibitionGalleryActionState()
}
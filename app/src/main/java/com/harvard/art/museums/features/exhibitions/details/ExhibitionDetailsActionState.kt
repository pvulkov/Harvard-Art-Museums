package com.harvard.art.museums.features.exhibitions.details

import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem

sealed class ExhibitionDetailsActionState {
    object LoadingState : ExhibitionDetailsActionState()
    data class ErrorState(val error: Throwable) : ExhibitionDetailsActionState()
    data class DataState(val exhibitionsList: List<ExhibitionDetailsViewItem>) : ExhibitionDetailsActionState()
}
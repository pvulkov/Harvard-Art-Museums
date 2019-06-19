package com.harvard.art.museums.features.search

import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem

sealed class SearchActionState {
    object LoadingState : SearchActionState()
    data class ErrorState(val error: Throwable) : SearchActionState()
    data class DataState(val exhibitionsList: List<ExhibitionDetailsViewItem>) : SearchActionState()
}
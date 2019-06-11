package com.harvard.art.museums.features.exhibitions

sealed class ExhibitionsActionState {
    object LoadingState : ExhibitionsActionState()
    data class ErrorState(val error: Throwable) : ExhibitionsActionState()
    data class DataState(val exhibitionsList: List<ExhibitionViewItem>) : ExhibitionsActionState()
}
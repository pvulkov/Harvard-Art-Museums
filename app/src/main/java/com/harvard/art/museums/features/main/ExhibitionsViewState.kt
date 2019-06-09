package com.harvard.art.museums.features.main

sealed class ExhibitionsViewState {
    object LoadingState : ExhibitionsViewState()
    data class DataState(val exhibitionItems: List<ExhibitionViewItem>) : ExhibitionsViewState()
    data class ErrorState(val error: Throwable) : ExhibitionsViewState()
}


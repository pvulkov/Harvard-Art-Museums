package com.harvard.art.museums.features.main

sealed class MainViewState {
    object LoadingState : MainViewState()
    data class DataState(val data: Any) : MainViewState()
    data class ErrorState(val error: Throwable) : MainViewState()
}


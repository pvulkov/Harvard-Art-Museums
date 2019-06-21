package com.harvard.art.museums.features.search


sealed class SearchActionState {
    object LoadingState : SearchActionState()
    data class ErrorState(val error: Throwable) : SearchActionState()
    data class DataState(val items: List<SearchResultViewItem>) : SearchActionState()
    data class FilterState(val filter: Filter) : SearchActionState()
}
package com.harvard.art.museums.features.search


sealed class SearchActionState {
    object LoadingState : SearchActionState()
    data class InitState(val items: List<SearchResultViewItem>, val filter: Filter) : SearchActionState()
    data class ErrorState(val error: Throwable) : SearchActionState()
    data class DataState(val items: List<SearchResultViewItem>) : SearchActionState()
    data class FilterState(val filter: Filter) : SearchActionState()
    data class RepeatSearchState(val text: String) : SearchActionState()
    data class OpenItemState(val data: Any) : SearchActionState()

}
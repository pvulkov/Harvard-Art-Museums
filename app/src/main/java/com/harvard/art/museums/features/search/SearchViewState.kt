package com.harvard.art.museums.features.search

import com.harvard.art.museums.ext.setData


data class SearchViewState(
        val state: State = State.INIT,
        val text: String? = null,
        val filter: Filter = Filter.UNKNOWN,
        val items: MutableList<SearchResultViewItem> = mutableListOf(),
        val error: Throwable? = null
) {

    enum class State { INIT, OPEN_ITEM, CHANGE_FILTER, REPEAT_SEARCH, SEARCHING, DATA, ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(state: SearchViewState) {

        private var state: State = state.state
        private var text: String? = state.text
        private var filter: Filter = state.filter
        private var items: MutableList<SearchResultViewItem> = state.items
        private var error: Throwable? = state.error


        fun text(text: String? = null): Builder {
            this.text = text
            return this
        }

        fun resultData(filter: Filter): Builder {
            this.filter = filter
            return this
        }

        fun resultData(data: List<SearchResultViewItem>): Builder {
            this.items.setData(data)
            return this
        }

        fun error(error: Throwable?): Builder {
            this.error = error
            return this
        }

        fun state(state: State): Builder {
            this.state = state
            return this
        }

        fun build(): SearchViewState = SearchViewState(state, text, filter, items, error)
    }
}


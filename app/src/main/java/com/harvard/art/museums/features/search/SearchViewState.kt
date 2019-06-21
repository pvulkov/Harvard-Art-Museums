package com.harvard.art.museums.features.search

import com.harvard.art.museums.ext.setData


data class SearchViewState(
        val state: State = State.INIT,
        val filter: Filter = Filter.UNKNOWN,
        val data: MutableList<String> = mutableListOf(),
        val error: Throwable? = null
) {

    enum class State { INIT, FILTER, SEARCH, DATA, ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(mainViewState: SearchViewState) {

        private var state: State = mainViewState.state
        private var filter: Filter = mainViewState.filter
        private var data: MutableList<String> = mainViewState.data
        private var error: Throwable? = mainViewState.error

        fun filterData(filter: Filter): Builder {
            this.filter = filter
            return this
        }

        fun filterData(data: MutableList<String>): Builder {
            this.data.setData(data)
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

        fun build(): SearchViewState = SearchViewState(state, filter, data, error)
    }
}


package com.harvard.art.museums.features.search

import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem


data class SearchViewState(
        val state: State = State.INIT,
        val exhibitionsList: List<ExhibitionDetailsViewItem> = listOf(),
        val error: Throwable? = null
) {

    enum class State { INIT, LOAD_MORE, DATA, ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(mainViewState: SearchViewState) {

        private var state: State = mainViewState.state
        private var exhibitionsList = mutableListOf<ExhibitionDetailsViewItem>()
        private var error: Throwable? = mainViewState.error


        fun exhibitionsData(data: List<ExhibitionDetailsViewItem>): Builder {
            this.exhibitionsList.setData(data)
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


        fun build(): SearchViewState =
                SearchViewState(state, exhibitionsList, error)
    }
}


package com.harvard.art.museums.features.exhibitions.details

import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem


data class ExhibitionDetailsViewState(
        val state: State = State.INIT,
        val exhibitionsList: List<ExhibitionDetailsViewItem> = listOf(),
        val error: Throwable? = null
) {

    enum class State { INIT, LOAD_MORE, DATA, ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(mainViewState: ExhibitionDetailsViewState) {

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


        fun build(): ExhibitionDetailsViewState =
                ExhibitionDetailsViewState(state, exhibitionsList, error)
    }
}


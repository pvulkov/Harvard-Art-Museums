package com.harvard.art.museums.features.exhibitions

import android.content.Intent
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem

data class ExhibitionsViewState(
        val state: State = State.INIT_DATA,
        val exhibitionItems: List<ExhibitionViewItem> = listOf(),
        val intent: Intent? = null,
        val error: Throwable? = null
) {

    enum class State { INIT_DATA, LOADING, DATA, ERROR, SHARE, OPEN_LINK,  NO_DATA }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(exhibitionsViewState: ExhibitionsViewState) {

        private var state: State = exhibitionsViewState.state
        private var exhibitionItems: List<ExhibitionViewItem> = exhibitionsViewState.exhibitionItems
        private var intent: Intent? = exhibitionsViewState.intent
        private var error: Throwable? = exhibitionsViewState.error


        fun exhibitionsData(exhibitionItems: List<ExhibitionViewItem>): Builder {
            this.exhibitionItems = exhibitionItems
            return this
        }

        fun intent(intent: Intent): Builder {
            this.intent = intent
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


        fun build(): ExhibitionsViewState =
                ExhibitionsViewState(state, exhibitionItems, intent, error)
    }

}


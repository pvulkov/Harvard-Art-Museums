package com.harvard.art.museums.features.exhibitions.details

import androidx.fragment.app.Fragment


data class ExhibitionDetailsViewState(
        val state: State = State.INIT,
        val data: Fragment? = null,
        val tag: String? = null,
        val error: Throwable? = null
) {

    enum class State { INIT, LOAD_MORE, DATA,  ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(mainViewState: ExhibitionDetailsViewState) {

        private var state: State = mainViewState.state
        private var data: Fragment? = mainViewState.data
        private var tag: String? = mainViewState.tag
        private var error: Throwable? = mainViewState.error


        fun exhibitionsData(data: Fragment?): Builder {
            this.data = data
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
                ExhibitionDetailsViewState(state, data, tag, error)
    }
}


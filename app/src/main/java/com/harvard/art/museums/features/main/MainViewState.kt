package com.harvard.art.museums.features.main

import androidx.fragment.app.Fragment


data class MainViewState(
        val state: State = State.INIT,
        val data: Fragment? = null,
        val tag: String? = null,
        val error: Throwable? = null
) {

    enum class State { INIT, NAVIGATION, ERROR, }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(mainViewState: MainViewState) {

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


        fun build(): MainViewState =
                MainViewState(state, data, tag, error)
    }
}


package com.harvard.art.museums.features.objects

data class ObjectsViewState(
        val state: State = State.INIT_DATA,
        val viewItems: List<ObjectViewItem> = listOf(),
        val error: Throwable? = null
) {

    enum class State { INIT_DATA, LOADING, DATA, ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(viewState: ObjectsViewState) {

        private var state: State = viewState.state
        private var viewItems: List<ObjectViewItem> = viewState.viewItems
        private var error: Throwable? = viewState.error


        fun viewItems(viewItems: List<ObjectViewItem>): Builder {
            this.viewItems = viewItems
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


        fun build(): ObjectsViewState = ObjectsViewState(state, viewItems, error)
    }

}


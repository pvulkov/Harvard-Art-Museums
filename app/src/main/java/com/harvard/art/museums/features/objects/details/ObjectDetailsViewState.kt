package com.harvard.art.museums.features.objects.details


data class ObjectDetailsViewState(
        val state: State = State.NONE,
        val objectData: ObjectDetailsViewItem? = null,
        val error: Throwable? = null
) {

    enum class State {
        @Deprecated("not used")
        NONE,
        LOAD, DATA, ERROR
    }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(viewState: ObjectDetailsViewState) {

        private var state: State = viewState.state
        private var objectData: ObjectDetailsViewItem? = viewState.objectData
        private var error: Throwable? = viewState.error


        fun objectData(objectData: ObjectDetailsViewItem): Builder {
            this.objectData = objectData
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


        fun build(): ObjectDetailsViewState =
                ObjectDetailsViewState(state, objectData, error)
    }
}


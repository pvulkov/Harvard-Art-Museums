package com.harvard.art.museums.features.objects

data class ObjectsViewState(
        val state: State = State.INIT_DATA,
        val data: Any? = null ,
        val error: Throwable? = null
) {

    enum class State { INIT_DATA, LOADING, DATA, ERROR, NO_DATA }


//    fun copy(): Builder {
//        return Builder(this)
//    }
//
//    class Builder(exhibitionsViewState: ObjectsViewState) {
//
//        private var state: State = exhibitionsViewState.state
//        private var exhibitionItems: List<ExhibitionViewItem> = exhibitionsViewState.exhibitionItems
//        private var error: Throwable? = exhibitionsViewState.error
//
//
//        fun resultData(exhibitionItems: List<ExhibitionViewItem>): Builder {
//            this.exhibitionItems = exhibitionItems
//            return this
//        }
//
//        fun error(error: Throwable?): Builder {
//            this.error = error
//            return this
//        }
//
//        fun state(state: State): Builder {
//            this.state = state
//            return this
//        }
//
//
//        fun build(): ObjectsViewState =
//                ObjectsViewState(state, exhibitionItems, error)
//    }

}


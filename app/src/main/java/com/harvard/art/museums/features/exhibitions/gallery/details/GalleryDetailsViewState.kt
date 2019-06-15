package com.harvard.art.museums.features.exhibitions.gallery.details

import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData


data class GalleryDetailsViewState(
        val state: State = State.NONE,
        val galleryObjectData: GalleryObjectData? = null,
        val error: Throwable? = null
) {

    enum class State { NONE, LOAD, DATA, ERROR }


    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(viewState: GalleryDetailsViewState) {

        private var state: State = viewState.state
        private var galleryObjectData: GalleryObjectData? = viewState.galleryObjectData
        private var error: Throwable? = viewState.error


        fun exhibitionsData(galleryObjectData: GalleryObjectData): Builder {
            this.galleryObjectData = galleryObjectData
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


        fun build(): GalleryDetailsViewState =
                GalleryDetailsViewState(state, galleryObjectData, error)
    }
}


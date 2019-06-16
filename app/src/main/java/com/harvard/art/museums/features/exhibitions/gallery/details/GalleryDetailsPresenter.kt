package com.harvard.art.museums.features.exhibitions.gallery.details

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.formatFromToDate
import com.harvard.art.museums.ext.formatLocation
import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData
import com.harvard.art.museums.features.exhibitions.data.ObjectDetails
import com.harvard.art.museums.features.exhibitions.data.Record
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsActionState as GalleryActionState
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsPresenter.GalleryDetailsView as GalleryView
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsViewState as GalleryViewState


class GalleryDetailsPresenter(view: GalleryView) : BasePresenter<GalleryView, GalleryViewState>(view) {


    override fun bindIntents() {

        val loadState: Observable<GalleryActionState> = intent(GalleryView::loadData)
                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
                .switchMap { loadExhibitionDetails(it) }
                .observeOn(AndroidSchedulers.mainThread())


        val noneState = GalleryViewState(state = GalleryViewState.State.NONE)
        val stateObservable = loadState
                .scan(noneState, this::viewStateReducer)
                .doOnError {
                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }


        subscribeViewState(stateObservable, GalleryView::render)
    }


    private fun viewStateReducer(previousState: GalleryViewState, currentState: GalleryActionState): GalleryViewState {

        return when (currentState) {

            GalleryActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(GalleryViewState.State.LOAD)
                        .error(null)
                        .build()
            }
            is GalleryActionState.DataState -> {
                previousState
                        .copy()
                        .state(GalleryViewState.State.DATA)
                        .exhibitionsData(currentState.data)
                        .error(null)
                        .build()
            }
            is GalleryActionState.ErrorState -> {
                previousState
                        .copy()
                        .state(GalleryViewState.State.ERROR)
                        .error(currentState.error)
                        .build()
            }
        }
    }


    private fun loadExhibitionDetails(exhibitionId: Int): Observable<GalleryActionState> {
        return getExhibitionsImageData(exhibitionId)
                .subscribeOn(Schedulers.io())
                .zipWith(getExhibitionsStoredData(exhibitionId), zipper)
                .toObservable()
                .map { toGalleryObjectData(it) }
                .map<GalleryActionState> { GalleryActionState.DataState(it) }
                .startWith(GalleryActionState.LoadingState)
                .onErrorReturn { GalleryActionState.ErrorState(it) }

    }


    private val zipper =
            BiFunction { o: ObjectDetails, e: ExhibitionRecord -> ObjectAndExhibitionData(o, e) }

    data class ObjectAndExhibitionData(val o: ObjectDetails, val e: ExhibitionRecord)

    private fun toGalleryObjectData(data: ObjectAndExhibitionData): GalleryObjectData {

        val imageList = mutableListOf<Image>()
        data.o.records.forEach { imageList.addAll(getImageList(it)) }

        return GalleryObjectData(data.e.title, imageList, data.e.poster, data.e.textiledescription, data.e.formatFromToDate(), data.e.formatLocation())
    }


    private fun getImageList(record: Record): List<Image> {

        return record.images
                ?.map { i -> Image(baseimageurl = i.baseimageurl, caption = record.title) }
                ?: emptyList()

    }

    private fun getExhibitionsImageData(exId: Int) = hamApi.getExhibitionsDetails(exId)

    private fun getExhibitionsStoredData(exId: Int) = hamDb.exhibitionRecordDao().getById(exId)

    interface GalleryDetailsView : BaseView {

        fun loadData(): Observable<Int>

        fun render(state: GalleryViewState)
    }

}


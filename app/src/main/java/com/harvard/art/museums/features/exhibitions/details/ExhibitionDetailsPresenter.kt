package com.harvard.art.museums.features.exhibitions.details

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.formatFromToDate
import com.harvard.art.museums.ext.formatLocation
import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData
import com.harvard.art.museums.data.pojo.ObjectDetails
import com.harvard.art.museums.data.pojo.Record
import com.harvard.art.museums.ext.EMPTY
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsPresenter.ExhibitionDetailsView as ExhibitionDetailsView
import  com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsViewState as ViewState
import  com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsActionState as ActionState


class ExhibitionDetailsPresenter(view: ExhibitionDetailsView) : BasePresenter<ExhibitionDetailsView, ViewState>(view) {


    override fun bindIntents() {

        val loadState: Observable<ActionState> = intent(ExhibitionDetailsView::loadData)
                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
                .switchMap { loadExhibitionDetails(it) }
                .observeOn(AndroidSchedulers.mainThread())


        val noneState = ViewState(state = ViewState.State.NONE)
        val stateObservable = loadState
                .scan(noneState, this::viewStateReducer)
                .doOnError {
                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }


        subscribeViewState(stateObservable, ExhibitionDetailsView::render)
    }


    private fun viewStateReducer(previousState: ViewState, currentState: ActionState): ViewState {

        return when (currentState) {

            ActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(ViewState.State.LOAD)
                        .error(null)
                        .build()
            }
            is ActionState.DataState -> {
                previousState
                        .copy()
                        .state(ViewState.State.DATA)
                        .exhibitionsData(currentState.data)
                        .error(null)
                        .build()
            }
            is ActionState.ErrorState -> {
                previousState
                        .copy()
                        .state(ViewState.State.ERROR)
                        .error(currentState.error)
                        .build()
            }
        }
    }


    private fun loadExhibitionDetails(exhibitionId: Int): Observable<ActionState> {
        return getExhibitionsImageData(exhibitionId)
                .subscribeOn(Schedulers.io())
                .zipWith(getExhibitionsStoredData(exhibitionId), zipper)
                .toObservable()
                .map { toGalleryObjectData(it) }
                .map<ActionState> { ActionState.DataState(it) }
                .startWith(ActionState.LoadingState)
                .onErrorReturn { ActionState.ErrorState(it) }

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
                ?.map { i -> Image(baseimageurl = i.baseimageurl, caption = record.title ?: EMPTY) }
                ?: emptyList()

    }

    private fun getExhibitionsImageData(exId: Int) = hamApi.getExhibitionsDetails(exId)

    private fun getExhibitionsStoredData(exId: Int) = hamDb.exhibitionRecordDao().getById(exId)

    interface ExhibitionDetailsView : BaseView {

        fun loadData(): Observable<Int>

        fun render(state: ViewState)
    }

}


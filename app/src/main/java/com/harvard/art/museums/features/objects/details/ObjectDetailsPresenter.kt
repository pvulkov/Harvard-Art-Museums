package com.harvard.art.museums.features.objects.details

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.*
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.exhibitions.data.GalleryObjectData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter.ObjectDetailsView as ObjectDetailsView
import  com.harvard.art.museums.features.objects.details.ObjectDetailsViewState as ViewState
import  com.harvard.art.museums.features.objects.details.ObjectDetailsActionState as ActionState


class ObjectDetailsPresenter(view: ObjectDetailsView) : BasePresenter<ObjectDetailsView, ViewState>(view) {


    override fun bindIntents() {

        val loadState: Observable<ActionState> = intent(ObjectDetailsView::loadData)
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


        subscribeViewState(stateObservable, ObjectDetailsView::render)
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
        return getExhibitionDetails(exhibitionId)
                .subscribeOn(Schedulers.io())
                .zipWith(getExhibitionsImageData(exhibitionId), zipper)
                .toObservable()
                .map { toGalleryObjectData(it) }
                .map<ActionState> { ActionState.DataState(it) }
                .startWith(ActionState.LoadingState)
                .onErrorReturn { ActionState.ErrorState(it) }

    }


    private val zipper =
            BiFunction { e: Exhibition, o: RecordsInfoData -> ExhibitionDetailsData(e, o) }

    data class ExhibitionDetailsData(val e: Exhibition, val o: RecordsInfoData)

    private fun toGalleryObjectData(data: ExhibitionDetailsData): GalleryObjectData {

        //TODO (pvalkov) make this an extension function
        return GalleryObjectData(
                data.e.title,
                getImageList(data.o),
                data.e.poster,
                data.e.textiledescription,
                data.e.begindate.fromServerDate().to_ddMMMMyyyy(),
                data.e.enddate.fromServerDate().to_ddMMMMyyyy()
        )
    }


    private fun getImageList(o: RecordsInfoData): List<Image> {

        val imageList = mutableListOf<Image>()
        o.records.forEach { imageList.addAll(getImageList(it)) }

        return imageList
    }


    private fun getImageList(record: Record): List<Image> {

        return record.images
                ?.map { i -> Image(baseimageurl = i.baseimageurl, caption = record.title ?: EMPTY) }
                ?: emptyList()

    }

    private fun getExhibitionsImageData(exId: Int) = hamApi.getExhibitionImages(exId)

    private fun getExhibitionDetails(exId: Int) = hamApi.getExhibitionDetails(exId)

    interface ObjectDetailsView : BaseView {

        fun loadData(): Observable<Int>

        fun render(state: ViewState)
    }

}


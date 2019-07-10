package com.harvard.art.museums.features.objects.details

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.*
import com.harvard.art.museums.ext.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter.ObjectDetailsView as ObjectDetailsView
import  com.harvard.art.museums.features.objects.details.ObjectDetailsViewState as ViewState
import  com.harvard.art.museums.features.objects.details.ObjectDetailsActionState as ActionState


class ObjectDetailsPresenter(view: ObjectDetailsView) : BasePresenter<ObjectDetailsView, ViewState>(view) {


    override fun bindIntents() {

        val loadState: Observable<ActionState> = intent(ObjectDetailsView::loadData)
                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
                .switchMap { loadObjectDetails(it) }
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
                        .objectData(currentState.itemObject)
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


    private fun loadObjectDetails(objectId: Int): Observable<ActionState> {
        return getObjectDetails(objectId)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .map { it.toObjectDetailsViewItem() }
                .map<ActionState> { ActionState.DataState(it) }
                .startWith(ActionState.LoadingState)
                .onErrorReturn { ActionState.ErrorState(it) }

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

    private fun getObjectDetails(obId: Int) = hamApi.getObjectDetails(obId)

    interface ObjectDetailsView : BaseView {

        fun loadData(): Observable<Int>

        fun render(state: ViewState)
    }

}


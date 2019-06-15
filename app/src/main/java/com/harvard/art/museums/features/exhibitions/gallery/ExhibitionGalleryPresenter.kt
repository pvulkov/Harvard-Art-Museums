package com.harvard.art.museums.features.exhibitions.gallery

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.ext.isValidUrl
import com.harvard.art.museums.ext.toExhibitionDetailsViewItem
import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import com.harvard.art.museums.features.exhibitions.data.ViewItemType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryActionState as ExdActionState
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter.ExhibitionsDetailsView as ExdView
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryViewState as ExdViewState


class ExhibitionGalleryPresenter(view: ExdView) : BasePresenter<ExdView, ExdViewState>(view) {


    override fun bindIntents() {


        val initState: Observable<ExdActionState> = intent(ExdView::initDataEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { loadInitialData() }
                .observeOn(AndroidSchedulers.mainThread())


        val loadMoreState: Observable<ExdActionState> = intent(ExdView::loadMoreEvent)
                .subscribeOn(Schedulers.io())
                .debounce(200, TimeUnit.MILLISECONDS)
                .map { it.item }
                .filter { it.next.isValidUrl() }
                .switchMap { loadInitialData() }
//                .switchMap { loadMoreData(it.next!!) }
                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
        val allViewState: Observable<ExdActionState> = Observable.merge(
                initState,
                loadMoreState
        )


        val initializeState = ExdViewState(state = ExdViewState.State.INIT)
        val stateObservable = allViewState
                .scan(initializeState, this::viewStateReducer)
                .doOnError {

                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }



        subscribeViewState(stateObservable, ExdView::render)
    }


    private fun viewStateReducer(previousState: ExdViewState, currentState: ExdActionState): ExdViewState {

        return when (currentState) {
            //TODO (pvalkov) review states
            ExdActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(ExdViewState.State.LOAD_MORE)
                        .error(null)
                        .build()
            }
            is ExdActionState.DataState -> {

//                Log.d("DEBUG", "DataState  items size -->  ${newList.size}")
                previousState
                        .copy()
                        .state(ExdViewState.State.DATA)
                        .exhibitionsData(currentState.exhibitionsList)
                        .error(null)
                        .build()
            }
            is ExdActionState.ErrorState -> {
                previousState
                        .copy()
                        .state(ExdViewState.State.ERROR)
                        .error(currentState.error)
                        .build()
            }

            //Nothing to do here
        }
    }


    private fun loadInitialData(): Observable<ExdActionState> {
        return getExhibitionsData()
                .map { toExhibitionItems(it) }
                .map<ExdActionState> { ExdActionState.DataState(it) }
                .startWith(ExdActionState.LoadingState)
                .onErrorReturn { ExdActionState.ErrorState(it) }

    }


    fun toExhibitionItems(exhibitions: List<ExhibitionRecord>): List<ExhibitionDetailsViewItem> {
        return exhibitions.map { it.toExhibitionDetailsViewItem() }
                .toMutableList()
                .also {
                    exhibitions.last().apply {
                        if (info.next.isValidUrl())
                            it.add(ExhibitionDetailsViewItem(ViewItemType.LOADER, exhibitionId = -1, next = info.next))

                    }
                }
    }


    private fun getExhibitionsData() = hamDb.exhibitionRecordDao().fetchAll().toObservable()

    interface ExhibitionsDetailsView : BaseView {

        fun initDataEvent(): Observable<Boolean>

        fun loadMoreEvent(): Observable<ViewItemAction>

        fun render(state: ExdViewState)
    }

}


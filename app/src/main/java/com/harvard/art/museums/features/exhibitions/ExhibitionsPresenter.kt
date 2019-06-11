package com.harvard.art.museums.features.exhibitions

import android.util.Log
import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.ext.isValidUrl
import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.ext.toExhibitionViewItem
import com.harvard.art.museums.ext.trimLoaders
import com.harvard.art.museums.features.exhibitions.ExhibitionsPresenter.ExhibitionsView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.exhibitions.ExhibitionsActionState as ExActionState
import com.harvard.art.museums.features.exhibitions.ExhibitionsViewState as ExViewState

class ExhibitionsPresenter(view: ExhibitionsView) : BasePresenter<ExhibitionsView, ExViewState>(view) {


    override fun bindIntents() {


        val initialState: Observable<ExActionState> = intent(ExhibitionsView::initDataEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { loadInitialData() }
                .observeOn(AndroidSchedulers.mainThread())


        // Load more restaurants
        val loadMoreState: Observable<ExActionState> = intent(ExhibitionsView::loadMoreEvent)
                .subscribeOn(Schedulers.io())
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter { it.next.isValidUrl() }
                .switchMap { loadMoreData(it.next!!) }
                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
        val allViewState: Observable<ExActionState> = Observable.merge(
                initialState,
                loadMoreState
        )


        val initializeState = ExViewState(state = ExViewState.State.INIT_DATA)
        val stateObservable = allViewState
                .scan(initializeState, this::viewStateReducer)
                .doOnError {

                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }



        subscribeViewState(stateObservable, ExhibitionsView::render)
    }

    private fun loadInitialData(): Observable<ExActionState> {
        return getCollection()
                .map { toExhibitionItems(it) }
                .map<ExActionState> { ExActionState.DataState(it) }
                .startWith(ExActionState.LoadingState)
                .onErrorReturn { ExActionState.ErrorState(it) }
    }


    private fun loadMoreData(url: String): Observable<ExActionState> {
        Log.d("DEBUG", "Load MORE.. " + url)
        return getNextCollection(url)
                .map { toExhibitionItems(it) }
                .map<ExActionState> { ExActionState.DataState(it) }
                .startWith(ExActionState.LoadingState)
                .onErrorReturn { ExActionState.ErrorState(it) }
    }


    private fun viewStateReducer(previousState: ExViewState, currentState: ExActionState): ExViewState {

        return when (currentState) {
            ExActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(ExViewState.State.INIT_DATA)
                        .error(null)
                        .build()
            }
            is ExActionState.DataState -> {

                val curList = previousState.exhibitionItems.trimLoaders()
                val newList = mutableListOf<ExhibitionViewItem>()
                newList.setData(curList.plus(currentState.exhibitionsList))

                Log.d("DEBUG", "DataState  items size -->  ${newList.size}")
                previousState
                        .copy()
                        .state(ExViewState.State.DATA)
                        .exhibitionsData(newList)
                        .error(null)
                        .build()
            }
            is ExActionState.ErrorState -> {
                previousState
                        .copy()
                        .state(ExViewState.State.ERROR)
                        .error(currentState.error)
                        .build()
            }
        }
    }

    private fun getNextCollection(url: String) = hamApi.getNextExhibitionsPage(url)

    private fun getCollection() = hamApi.getExhibitions()


    private fun toExhibitionItems(exhibitions: Exhibitions): List<ExhibitionViewItem> {
        return exhibitions.records.map { it.toExhibitionViewItem() }
                .toMutableList()
                .also {
                    if (exhibitions.info.next.isValidUrl())
                        it.add(ExhibitionViewItem(ViewItemType.LOADER, next = exhibitions.info.next))
                }
    }


    interface ExhibitionsView : BaseView {

        fun initDataEvent(): Observable<Boolean>

        fun loadMoreEvent(): Observable<ExhibitionViewItem>

        fun render(state: ExViewState)

    }

}


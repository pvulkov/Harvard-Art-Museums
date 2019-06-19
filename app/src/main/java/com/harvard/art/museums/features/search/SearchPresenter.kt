package com.harvard.art.museums.features.search

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import com.harvard.art.museums.features.search.SearchPresenter.SearchView
import io.reactivex.Observable


class SearchPresenter(view: SearchView) : BasePresenter<SearchView, SearchViewState>(view) {


    override fun bindIntents() {


//        val initState: Observable<ExdActionState> = intent(ExdView::initDataEvent)
//                .subscribeOn(Schedulers.io())
//                .debounce(400, TimeUnit.MILLISECONDS)
//                .switchMap { loadInitialData() }
//                .observeOn(AndroidSchedulers.mainThread())
//
//
//        val loadMoreState: Observable<ExdActionState> = intent(ExdView::loadMoreEvent)
//                .subscribeOn(Schedulers.io())
//                .debounce(200, TimeUnit.MILLISECONDS)
//                .map { it.item }
//                .filter { it.next.isValidUrl() }
//                .switchMap { loadInitialData() }
////                .switchMap { loadMoreData(it.next!!) }
//                .observeOn(AndroidSchedulers.mainThread())
//
//
//        //NOTE (pvalkov) merge states here
//        val allViewState: Observable<ExdActionState> = Observable.merge(
//                initState,
//                loadMoreState
//        )
//
//
//        val initializeState = ExdViewState(state = ExdViewState.State.INIT)
//        val stateObservable = allViewState
//                .scan(initializeState, this::viewStateReducer)
//                .doOnError {
//
//                    //TODO (pvalkov) implement crashlytics
//                    it.printStackTrace()
//                }
//
//
//
//        subscribeViewState(stateObservable, ExdView::render)
    }


    private fun viewStateReducer(previousState: SearchViewState, currentState: SearchActionState): SearchViewState {

        return when (currentState) {
            //TODO (pvalkov) review states
            SearchActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.LOAD_MORE)
                        .error(null)
                        .build()
            }
            is SearchActionState.DataState -> {

//                Log.d("DEBUG", "DataState  items size -->  ${newList.size}")
                previousState
                        .copy()
                        .state(SearchViewState.State.DATA)
                        .exhibitionsData(currentState.exhibitionsList)
                        .error(null)
                        .build()
            }
            is SearchActionState.ErrorState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.ERROR)
                        .error(currentState.error)
                        .build()
            }

            //Nothing to do here
        }
    }


//    private fun loadInitialData(): Observable<ExdActionState> {
//        return getExhibitionsData()
//                .map { toExhibitionItems(it) }
//                .map<ExdActionState> { ExdActionState.DataState(it) }
//                .startWith(ExdActionState.LoadingState)
//                .onErrorReturn { ExdActionState.ErrorState(it) }
//
//    }



//    private fun getExhibitionsData() = hamDb.exhibitionRecordDao().fetchAll().toObservable()


    interface SearchView : BaseView {

//        fun initDataEvent(): Observable<Boolean>
//
//        fun loadMoreEvent(): Observable<ViewItemAction>

        fun render(state: SearchViewState)
    }

}


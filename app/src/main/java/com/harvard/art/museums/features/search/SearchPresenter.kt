package com.harvard.art.museums.features.search

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.ObjectDetails
import com.harvard.art.museums.features.search.SearchPresenter.SearchView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchPresenter(view: SearchView) : BasePresenter<SearchView, SearchViewState>(view) {


    override fun bindIntents() {

        //TODO (pvalkov) load previous searches if exists


        val filterState: Observable<SearchActionState> = intent(SearchView::filterEvent)
                .subscribeOn(Schedulers.io())
                .switchMap { applyFilter(it.filter) }
                .observeOn(AndroidSchedulers.mainThread())


        val searchState: Observable<SearchActionState> = intent(SearchView::searchEvent)
                .subscribeOn(Schedulers.io())
//                .filter { it.next.isValidUrl() }
                .switchMap { searchObjects(it.text!!) }
                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
        val allViewState: Observable<SearchActionState> = Observable.merge(
                searchState,
                filterState
        )


        val initializeState = SearchViewState(state = SearchViewState.State.INIT)
        val stateObservable = allViewState
                .scan(initializeState, this::viewStateReducer)
                .doOnError {

                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }


        subscribeViewState(stateObservable, SearchView::render)
    }


    private fun viewStateReducer(previousState: SearchViewState, currentState: SearchActionState): SearchViewState {

        return when (currentState) {
            //TODO (pvalkov) review states
            SearchActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.SEARCH)
                        .error(null)
                        .build()
            }
            is SearchActionState.DataState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.DATA)
                        .filterData(currentState.items.toMutableList())
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
            is SearchActionState.FilterState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.FILTER)
                        .filterData(currentState.filter)
                        .build()
            }
        }
    }


    private fun applyFilter(filter: Filter): Observable<SearchActionState> {
        return Observable.just(SearchActionState.FilterState(filter))
    }

    private fun searchObjects(text: String): Observable<SearchActionState> {
        return hamApi.searchObjectByKeyword(text)
                .toObservable()
                .map { toSearchViewItems(it) }
                .map<SearchActionState> { SearchActionState.DataState(it) }
                .startWith(SearchActionState.LoadingState)
                .onErrorReturn { SearchActionState.ErrorState(it) }
    }

    private fun toSearchViewItems(data: ObjectDetails): List<String> {

        val result = mutableListOf<String>()

        var idx = 0
        data.records.forEach { result.add(" item ${idx++}") }


        return result
    }


//    private fun getExhibitionsData() = hamDb.exhibitionRecordDao().fetchAll().toObservable()


    interface SearchView : BaseView {

        //        fun initDataEvent(): Observable<Boolean>
//
        fun searchEvent(): Observable<SearchViewAction>

        fun filterEvent(): Observable<SearchViewAction>

        fun render(state: SearchViewState)
    }

}


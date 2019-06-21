package com.harvard.art.museums.features.search

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.data.pojo.ObjectDetails
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.features.search.SearchPresenter.SearchView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.lang.Exception


class SearchPresenter(view: SearchView) : BasePresenter<SearchView, SearchViewState>(view) {


    override fun bindIntents() {

        //TODO (pvalkov) load previous searches if exists


        val filterState: Observable<SearchActionState> = intent(SearchView::filterEvent)
                .subscribeOn(Schedulers.io())
                .switchMap { applyFilter(it.filter) }
                .observeOn(AndroidSchedulers.mainThread())


        val zipper =
                BiFunction { o: String, e: Filter -> TextAndFilter(o, e) }


        val searchState: Observable<SearchActionState> = intent(SearchView::searchEvent)
                .map { it.text!! }
                .withLatestFrom(intent(SearchView::filterEvent).map { it.filter }, zipper)
                .subscribeOn(Schedulers.io())
                .switchMap { searchObjects(it) }
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
                        .resultData(currentState.items.toMutableList())
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
                        .resultData(currentState.filter)
                        .build()
            }
        }
    }


    private fun applyFilter(filter: Filter): Observable<SearchActionState> {
        return Observable.just(SearchActionState.FilterState(filter))
    }

    private fun searchObjects(tf: TextAndFilter): Observable<SearchActionState> {

        return searchData(tf)
                .map<SearchActionState> { SearchActionState.DataState(it) }
                .startWith(SearchActionState.LoadingState)
                .onErrorReturn { SearchActionState.ErrorState(it) }
    }


    private fun searchData(tf: TextAndFilter): Observable<List<SearchResultViewItem>> {

        return when (tf.filter) {
            Filter.EXHIBITION -> hamApi.getExhibitionsByKeyword(tf.text).map { toSearchViewItems(it) }.toObservable()
            Filter.OBJECTS -> hamApi.searchObjectByKeyword(tf.text).map { toSearchViewItems(it) }.toObservable()

            else -> throw Exception("Unhandled else case")
        }
    }

    private fun toSearchViewItems(data: ObjectDetails): List<SearchResultViewItem> {

        val result = mutableListOf<SearchResultViewItem>()

        data.records.forEach {
            val item = SearchResultViewItem(
                    SearchResultViewType.OBJECT,
                    it.title ?: EMPTY,
                    it.primaryimageurl,
                    1)
            result.add(item)
        }


        return result
    }


    private fun toSearchViewItems(data: Exhibitions): List<SearchResultViewItem> {

        val result = mutableListOf<SearchResultViewItem>()

        data.records.forEach {
            val item = SearchResultViewItem(
                    SearchResultViewType.EXHIBITION,
                    it.title ?: EMPTY,
                    "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    2)
            result.add(item)
        }


        return result
    }


//    private fun getExhibitionsData() = hamDb.exhibitionRecordDao().fetchAll().toObservable()

    private data class TextAndFilter(val text: String, val filter: Filter)

    interface SearchView : BaseView {

        //        fun initDataEvent(): Observable<Boolean>
//
        fun searchEvent(): Observable<SearchViewAction>

        fun filterEvent(): Observable<SearchViewAction>

        fun render(state: SearchViewState)
    }

}


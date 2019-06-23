package com.harvard.art.museums.features.search

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.RecentSearchRecord
import com.harvard.art.museums.ext.toSearchViewItems
import com.harvard.art.museums.features.search.SearchPresenter.SearchView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class SearchPresenter(view: SearchView) : BasePresenter<SearchView, SearchViewState>(view) {

    private val zipper = BiFunction { o: String, e: Filter -> TextAndFilter(o, e) }

    override fun bindIntents() {

        val initState: Observable<SearchActionState> = intent(SearchView::initEvent)
                .observeOn(Schedulers.io())
                .switchMap { initData(it.filter) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val filterState: Observable<SearchActionState> = intent(SearchView::filterEvent)
                .subscribeOn(Schedulers.io())
                .switchMap { applyFilter(it.filter) }
                .observeOn(AndroidSchedulers.mainThread())

        val searchState: Observable<SearchActionState> = intent(SearchView::searchEvent)
                .doOnNext { updateRecentSearches(it) }
                .map { it.text!! }
                .withLatestFrom(intent(SearchView::filterEvent).map { it.filter }, zipper)
                .subscribeOn(Schedulers.io())
                .switchMap { searchObjects(it) }
                .observeOn(AndroidSchedulers.mainThread())


        val itemViewState: Observable<SearchActionState> = intent(SearchView::itemViewsEvents)
                .observeOn(Schedulers.io())
                .doOnNext { updateRecentSearches(it) }
                .switchMap { viewItemState(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val allViewState = Observable.merge(initState, searchState, filterState, itemViewState)

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
            is SearchActionState.InitState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.INIT)
                        .resultData(currentState.filter)
                        .resultData(currentState.items.toMutableList())
                        .error(null)
                        .build()
            }
            is SearchActionState.RepeatSearchState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.REPEAT_SEARCH)
                        .text(currentState.text)
                        .error(null)
                        .build()
            }
            is SearchActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.SEARCHING)
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
                        .state(SearchViewState.State.CHANGE_FILTER)
                        .resultData(currentState.filter)
                        .build()
            }

            is SearchActionState.OpenItemState -> {
                previousState
                        .copy()
                        .state(SearchViewState.State.OPEN_ITEM)
                        //TODO (pvalkov) finish implementation
//                        .resultData(currentState.filter)
                        .build()
            }
        }
    }


    private fun viewItemState(item: SearchResultViewItem): Observable<SearchActionState> {
        return when (item.viewType) {
            SearchResultViewType.RECENT -> Observable.just(SearchActionState.RepeatSearchState(item.text))
            SearchResultViewType.EXHIBITION -> Observable.just(SearchActionState.OpenItemState(item))
            SearchResultViewType.OBJECT -> Observable.just(SearchActionState.OpenItemState(item))
        }
    }

    private fun initData(filter: Filter): Observable<SearchActionState> {
        return Single.just(hamDb.recentSearchesDao().fetchAll())
                .observeOn(Schedulers.io())
                .map { it.toSearchViewItems() }
                .map<SearchActionState> { SearchActionState.InitState(it, filter) }
                .toObservable()
    }

    private fun applyFilter(filter: Filter): Observable<SearchActionState> {
        return Observable.just(SearchActionState.FilterState(filter))
    }


    private fun updateRecentSearches(sva: SearchResultViewItem) {

        val recentList = hamDb.recentSearchesDao().fetchAll().toMutableList()
        //NOTE (pvalkov) if record exists just update timestamp
        recentList
                .firstOrNull { it.text.equals(sva.text, true) }
                ?.apply { this.timestamp = System.currentTimeMillis() }
                ?: run { recentList.add(RecentSearchRecord(text = sva.text!!)) }


        val newList = recentList.take(10).sortedBy { it.timestamp }

        hamDb.recentSearchesDao().insert(newList)
    }


    private fun updateRecentSearches(sva: SearchViewAction) {
        //NOTE (pvalkov) update recent searches only if user clicked "search" button
        if (!sva.isSubmitted)
            return

        val recentList = hamDb.recentSearchesDao().fetchAll().toMutableList()
        //NOTE (pvalkov) if record exists just update timestamp
        recentList
                .firstOrNull { it.text.equals(sva.text, true) }
                ?.apply { this.timestamp = System.currentTimeMillis() }
                ?: run { recentList.add(RecentSearchRecord(text = sva.text!!)) }


        val newList = recentList.take(10).sortedBy { it.timestamp }

        hamDb.recentSearchesDao().insert(newList)
    }


    private fun searchObjects(tf: TextAndFilter): Observable<SearchActionState> {
        return if (tf.text.isEmpty())
            initData(tf.filter)
        else
            searchData(tf)
                    .map<SearchActionState> { SearchActionState.DataState(it) }
                    .startWith(SearchActionState.LoadingState)
                    .onErrorReturn { SearchActionState.ErrorState(it) }
    }


    private fun searchData(tf: TextAndFilter): Observable<List<SearchResultViewItem>> {

        return when (tf.filter) {
            Filter.EXHIBITION -> hamApi.getExhibitionsByKeyword(tf.text).map { it.toSearchViewItems() }.toObservable()
            Filter.OBJECTS -> hamApi.searchObjectByKeyword(tf.text).map { it.toSearchViewItems() }.toObservable()
            else -> throw Exception("Unhandled else case")
        }
    }


    private data class TextAndFilter(val text: String, val filter: Filter)

    interface SearchView : BaseView {

        fun itemViewsEvents(): Observable<SearchResultViewItem>

        fun initEvent(): Observable<SearchViewAction>

        fun searchEvent(): Observable<SearchViewAction>

        fun filterEvent(): Observable<SearchViewAction>

        fun render(state: SearchViewState)
    }

}
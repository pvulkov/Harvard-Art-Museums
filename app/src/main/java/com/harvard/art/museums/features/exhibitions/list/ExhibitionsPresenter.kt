package com.harvard.art.museums.features.exhibitions.list

import android.util.Log
import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.repositories.ExhibitionsRepository
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import com.harvard.art.museums.features.exhibitions.data.ViewItemType
import com.harvard.art.museums.features.exhibitions.data.ViewItemType.ViewType
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsPresenter.ExhibitionsView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsActionState as ExActionState
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsViewState as ExViewState


class ExhibitionsPresenter(view: ExhibitionsView) : BasePresenter<ExhibitionsView, ExViewState>(view) {


    private val repository = ExhibitionsRepository(hamApi, hamDb)

    override fun bindIntents() {

        val initialState: Observable<ExActionState> = intent(ExhibitionsView::initDataEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { loadInitialData() }
                .observeOn(AndroidSchedulers.mainThread())


        val loadMoreState: Observable<ExActionState> = intent(ExhibitionsView::loadMoreEvent)
                .subscribeOn(Schedulers.io())
                .debounce(200, TimeUnit.MILLISECONDS)
                .map { it.item }
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
        return repository.getExhibitions()
                .map { toExhibitionItems(it) }
                .map<ExActionState> { ExActionState.DataState(it) }
                .startWith(ExActionState.LoadingState)
                .onErrorReturn { ExActionState.ErrorState(it) }
    }


    private fun loadMoreData(url: String): Observable<ExActionState> {
        Log.d("DEBUG", "Load MORE.. " + url)
        return Observable.just(url)
                .filter { it.isValidUrl() }
                .flatMap { repository.getNextExhibitions(it) }
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
                        .state(ExViewState.State.LOADING)
                        .error(null)
                        .build()
            }
            is ExActionState.DataState -> {

                //TODO (pvalkov) if two list are identical we should not recreate the UI. Think of a way to do it
                val curList = previousState.exhibitionItems.trimLoaders()
                val newList = mutableListOf<ExhibitionViewItem>()
                newList.setData(curList.update(currentState.exhibitionsList))

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

            else -> throw  Exception("Unhabdled case instate reducer")
        }
    }


    private fun toExhibitionItems(exhibitions: List<ExhibitionRecord>): List<ExhibitionViewItem> {
        return exhibitions.map { it.toExhibitionViewItem() }
                .toMutableList()
                .also {
                    exhibitions.last().apply {
                        if (info.next.isValidUrl())
                            it.add(ExhibitionViewItem(ViewType.LOADER, exhibitionId = -1, next = info.next))

                    }
                }
    }


    interface ExhibitionsView : BaseView {

        fun initDataEvent(): Observable<Boolean>

        fun loadMoreEvent(): Observable<ViewItemAction>

        fun render(state: ExViewState)

    }

}


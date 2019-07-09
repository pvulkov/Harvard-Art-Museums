package com.harvard.art.museums.features.objects.list

import android.util.Log
import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.data.pojo.ObjectRecord
import com.harvard.art.museums.data.repositories.ObjectsRepository
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.exhibitions.data.ViewItemType.ViewType
import com.harvard.art.museums.features.objects.list.ObjectsPresenter.ObjectsView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.objects.list.ObjectsActionState as ActionState
import com.harvard.art.museums.features.objects.list.ObjectsViewState as ViewState


class ObjectsPresenter(view: ObjectsView) : BasePresenter<ObjectsView, ViewState>(view) {

    private val repository = ObjectsRepository(hamApi, hamDb)

    override fun bindIntents() {

        val initialState: Observable<ActionState> = intent(ObjectsView::initDataEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { loadInitialData() }
                .observeOn(AndroidSchedulers.mainThread())


        val loadMoreState: Observable<ActionState> = intent(ObjectsView::loadMoreEvent)
                .subscribeOn(Schedulers.io())
                .debounce(200, TimeUnit.MILLISECONDS)
                .map { it.nextUrl }
                .filter { it.isValidUrl() }
                .switchMap { loadMoreData(it) }
                .observeOn(AndroidSchedulers.mainThread())


        val allViewState: Observable<ActionState> = Observable.merge(
                initialState,
                loadMoreState
        )


        val initializeState = ViewState(state = ViewState.State.INIT_DATA)
        val stateObservable = allViewState
                .scan(initializeState, this::viewStateReducer)
                .doOnError {
                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }

        subscribeViewState(stateObservable, ObjectsView::render)
    }


    private fun viewStateReducer(previousState: ViewState, currentState: ActionState): ViewState {

        return when (currentState) {
            ActionState.LoadingState -> {
                previousState
                        .copy()
                        .state(ViewState.State.LOADING)
                        .error(null)
                        .build()
            }
            is ActionState.DataState -> {

                //TODO (pvalkov) if two list are identical we should not recreate the UI. Think of a way to do it
                val curList = previousState.viewItems.trimLoaders()
                val newList = mutableListOf<ObjectViewItem>()
                newList.setData(curList.update(currentState.viewItems))

                Log.d("DEBUG", "DataState  items size -->  ${curList.size}")
                previousState
                        .copy()
                        .state(ViewState.State.DATA)
                        .viewItems(newList)
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

            else -> throw  Exception("Unhandled case inside reducer")
        }
    }


    private fun loadInitialData(): Observable<ActionState> {
        return repository.getObjects()
                .map { toObjectsViewItems(it) }
                .map<ActionState> { ActionState.DataState(it) }
                .startWith(ActionState.LoadingState)
                .onErrorReturn { ActionState.ErrorState(it) }
    }


    private fun loadMoreData(url: String): Observable<ActionState> {
        Log.d("DEBUG", "Load MORE.. " + url)
        return repository.getNextObjects(url)
                .map { toObjectsViewItems(it) }
                .map<ActionState> { ActionState.DataState(it) }
                .onErrorReturn { ActionState.ErrorState(it) }
    }

    private fun toObjectsViewItems(records: List<ObjectRecord>): List<ObjectViewItem> {

        return records
                .map {
                    ObjectViewItem(
                            ViewType.DATA,
                            it.id,
                            it.title ?: EMPTY,
                            it.objectnumber,
                            getPeople(it),
                            it.classification ?: EMPTY,
                            it.images.firstOrNull(),
                            it.nextUrl
                    )
                }
                .toMutableList()
                .also {
                    it.last().nextUrl.isValidUrl().ifTrue {
                        it.add(ObjectViewItem(ViewType.LOADER, nextUrl = it.last().nextUrl))
                    }
                }
    }


    private fun getPeople(data: ObjectRecord): String {
        return data.people.joinToString(", ", EMPTY, EMPTY) { it.displayname }
    }


    interface ObjectsView : BaseView {

        fun initDataEvent(): Observable<Boolean>

        fun loadMoreEvent(): Observable<ObjectViewItem>

        fun render(state: ViewState)

    }

}
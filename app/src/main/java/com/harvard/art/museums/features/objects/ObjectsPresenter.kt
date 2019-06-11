package com.harvard.art.museums.features.objects

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.objects.ObjectsViewState as OViewState
import com.harvard.art.museums.features.objects.ObjectsPresenter.ObjectsView

class ObjectsPresenter(view: ObjectsView) : BasePresenter<ObjectsView, OViewState>(view) {


    override fun bindIntents() {


        val initialState: Observable<OViewState> = intent(ObjectsView::initDataEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { Observable.just(OViewState()) }
                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
//        val allViewState: Observable<ObjectsView> = Observable.merge(
//                initialState,
//                loadMoreState
//        )


        val initializeState = OViewState(state = OViewState.State.INIT_DATA)
        val stateObservable = initialState
                .scan(initializeState, this::viewStateReducer)
                .doOnError {

                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }



        subscribeViewState(stateObservable, ObjectsView::render)
    }


    private fun viewStateReducer(previousState: OViewState, currentState: OViewState): OViewState {

        return currentState
    }


    interface ObjectsView : BaseView {

        fun initDataEvent(): Observable<Boolean>

        fun render(state: OViewState)

    }

}


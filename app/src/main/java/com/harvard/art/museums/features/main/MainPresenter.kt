package com.harvard.art.museums.features.main

import android.util.Log
import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.exhibitions.ExhibitionsFragment
import com.harvard.art.museums.features.main.MainPresenter.MainView
import com.harvard.art.museums.features.main.data.NavigationAction
import com.harvard.art.museums.features.objects.ObjectsFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainPresenter(view: MainView) : BasePresenter<MainView, MainViewState>(view) {


    override fun bindIntents() {


//        val helloWorldState: Observable<ExhibitionsViewState> = intent(ExhibitionsView::sayHelloWorldIntent)
//                .subscribeOn(Schedulers.io())
//                .debounce(400, TimeUnit.MILLISECONDS)
//                .switchMap { ExhibitionsUseCase.loadInitialData(hamApi) }
//                .doOnNext { Timber.d("Received new state: " + it) }
//                .observeOn(AndroidSchedulers.mainThread())


        val navigationState: Observable<MainViewState> = intent(MainView::navigationEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { toActionState(it) }
                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
//        val allViewState: Observable<ExhibitionsActionState> = Observable.merge(
//                initialState,
//                navigationState
//        )

        val initState = MainViewState(
                MainViewState.State.INIT,
                ExhibitionsFragment(),
                NavigationAction.EXHIBITIONS.tag
        )

        val stateObservable = navigationState
                .scan(initState, this::viewStateReducer)
                .doOnError {

                    //TODO (pvalkov) implement crashlytics
                    it.printStackTrace()
                }


//        subscribeViewState(stateObservable, ExhibitionsPresenter.ExhibitionsView::render)


        subscribeViewState(stateObservable, MainView::render)
    }


    private fun viewStateReducer(previousState: MainViewState, currentState: MainViewState): MainViewState {

        //Nothing to do here
        return currentState
    }


    private fun toActionState(action: NavigationAction): Observable<MainViewState> {

        val state = when (action) {
            NavigationAction.EXHIBITIONS -> MainViewState(MainViewState.State.NAVIGATION, ExhibitionsFragment(), action.tag, null)
            NavigationAction.OBJECTS -> MainViewState(MainViewState.State.NAVIGATION, ObjectsFragment(), action.tag, null)
        }

        Log.d("DEBUG", "action >> " + action)
        return Observable.just(state)
    }

    interface MainView : BaseView {

        fun navigationEvent(): Observable<NavigationAction>

        fun render(state: MainViewState)
    }

}


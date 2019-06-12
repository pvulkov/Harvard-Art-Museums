package com.harvard.art.museums.features.home

import android.util.Log
import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.exhibitions.main.ExhibitionsFragment
import com.harvard.art.museums.features.home.HomePresenter.HomeView
import com.harvard.art.museums.features.home.data.NavigationAction
import com.harvard.art.museums.features.objects.ObjectsFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HomePresenter(view: HomeView) : BasePresenter<HomeView, HomeViewState>(view) {


    override fun bindIntents() {


//        val helloWorldState: Observable<ExhibitionsViewState> = intent(ExhibitionsView::sayHelloWorldIntent)
//                .subscribeOn(Schedulers.io())
//                .debounce(400, TimeUnit.MILLISECONDS)
//                .switchMap { ExhibitionsUseCase.loadInitialData(hamApi) }
//                .doOnNext { Timber.d("Received new state: " + it) }
//                .observeOn(AndroidSchedulers.mainThread())


        val navigationState: Observable<HomeViewState> = intent(HomeView::navigationEvent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { toActionState(it) }
                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
//        val allViewState: Observable<ExhibitionsActionState> = Observable.merge(
//                initialState,
//                navigationState
//        )

        val initState = HomeViewState(
                HomeViewState.State.INIT,
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


        subscribeViewState(stateObservable, HomeView::render)
    }


    private fun viewStateReducer(previousState: HomeViewState, currentState: HomeViewState): HomeViewState {

        //Nothing to do here
        return currentState
    }


    private fun toActionState(action: NavigationAction): Observable<HomeViewState> {

        val state = when (action) {
            NavigationAction.EXHIBITIONS -> HomeViewState(HomeViewState.State.NAVIGATION, ExhibitionsFragment(), action.tag, null)
            NavigationAction.OBJECTS -> HomeViewState(HomeViewState.State.NAVIGATION, ObjectsFragment(), action.tag, null)
        }

        Log.d("DEBUG", "action >> " + action)
        return Observable.just(state)
    }

    interface HomeView : BaseView {

        fun navigationEvent(): Observable<NavigationAction>

        fun render(state: HomeViewState)
    }

}


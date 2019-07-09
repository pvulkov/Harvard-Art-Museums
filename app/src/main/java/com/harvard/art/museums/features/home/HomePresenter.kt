package com.harvard.art.museums.features.home

import android.util.Log
import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsFragment
import com.harvard.art.museums.features.home.HomePresenter.HomeView
import com.harvard.art.museums.features.home.data.NavigationAction
import com.harvard.art.museums.features.objects.list.ObjectsFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HomePresenter(view: HomeView) : BasePresenter<HomeView, HomeViewState>(view) {


    override fun bindIntents() {



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

        Log.d("DEBUG", "filter >> " + action)
        return Observable.just(state)
    }

    interface HomeView : BaseView {

        fun navigationEvent(): Observable<NavigationAction>

        fun render(state: HomeViewState)
    }

}


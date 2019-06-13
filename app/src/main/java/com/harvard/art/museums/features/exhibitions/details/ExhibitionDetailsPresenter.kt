package com.harvard.art.museums.features.exhibitions.details

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsPresenter.ExhibitionsDetailsView as ExdView
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsViewState as ExdViewState


class ExhibitionDetailsPresenter(view: ExdView) : BasePresenter<ExdView, ExdViewState>(view) {


    override fun bindIntents() {


//        val helloWorldState: Observable<ExhibitionsViewState> = intent(ExhibitionsView::sayHelloWorldIntent)
//                .subscribeOn(Schedulers.io())
//                .debounce(400, TimeUnit.MILLISECONDS)
//                .switchMap { ExhibitionsUseCase.loadInitialData(hamApi) }
//                .doOnNext { Timber.d("Received new state: " + it) }
//                .observeOn(AndroidSchedulers.mainThread())


//        val navigationState: Observable<ExdViewState> = intent(HomeView::navigationEvent)
//                .subscribeOn(Schedulers.io())
//                .debounce(400, TimeUnit.MILLISECONDS)
//                .switchMap { toActionState(it) }
//                .observeOn(AndroidSchedulers.mainThread())


        //NOTE (pvalkov) merge states here
//        val allViewState: Observable<ExhibitionsActionState> = Observable.merge(
//                initialState,
//                navigationState
//        )

//        val initState = ExdViewState(
//                HomeViewState.State.INIT,
//                ExhibitionsFragment(),
//                NavigationAction.EXHIBITIONS.tag
//        )
//
//        val stateObservable = navigationState
//                .scan(initState, this::viewStateReducer)
//                .doOnError {
//
//                    //TODO (pvalkov) implement crashlytics
//                    it.printStackTrace()
//                }


//        subscribeViewState(stateObservable, ExhibitionsPresenter.ExhibitionsView::render)


//        subscribeViewState(stateObservable, HomeView::render)
    }


    private fun viewStateReducer(previousState: ExdViewState, currentState: ExdViewState): ExdViewState {

        //Nothing to do here
        return currentState
    }




    interface ExhibitionsDetailsView : BaseView {


        fun render(state: ExdViewState)
    }

}


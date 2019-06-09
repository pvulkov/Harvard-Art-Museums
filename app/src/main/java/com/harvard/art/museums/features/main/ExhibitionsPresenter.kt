package com.harvard.art.museums.features.main

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

import com.harvard.art.museums.features.main.ExhibitionsPresenter.ExhibitionsView

class ExhibitionsPresenter(view: ExhibitionsView) : BasePresenter<ExhibitionsView, ExhibitionsViewState>(view) {


    override fun bindIntents() {


        val helloWorldState: Observable<ExhibitionsViewState> = intent(ExhibitionsView::sayHelloWorldIntent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { ExhibitionsUseCase.getHelloWorldText(hamApi) }
                .doOnNext { Timber.d("Received new state: " + it) }
                .observeOn(AndroidSchedulers.mainThread())


        subscribeViewState(helloWorldState, ExhibitionsView::render)
    }



    interface ExhibitionsView : BaseView {

        fun sayHelloWorldIntent(): Observable<Unit>

        fun render(state: ExhibitionsViewState)
    }

}


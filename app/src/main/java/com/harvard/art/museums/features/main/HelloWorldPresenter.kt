package com.harvard.art.museums.features.main

import com.harvard.art.museums.base.BasePresenter
import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.domain.GetHelloWorldTextUseCase
import com.harvard.art.museums.domain.HelloWorldViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

import com.harvard.art.museums.features.main.HelloWorldPresenter.HelloWorldView

class HelloWorldPresenter(view: HelloWorldView) : BasePresenter<HelloWorldView, HelloWorldViewState>(view) {


    override fun bindIntents() {


        val helloWorldState: Observable<HelloWorldViewState> = intent(HelloWorldView::sayHelloWorldIntent)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .switchMap { GetHelloWorldTextUseCase.getHelloWorldText() }
                .doOnNext { Timber.d("Received new state: " + it) }
                .observeOn(AndroidSchedulers.mainThread())


        subscribeViewState(helloWorldState, HelloWorldView::render)
    }


    interface HelloWorldView : BaseView {

        fun sayHelloWorldIntent(): Observable<Unit>

        fun render(state: HelloWorldViewState)
    }

}


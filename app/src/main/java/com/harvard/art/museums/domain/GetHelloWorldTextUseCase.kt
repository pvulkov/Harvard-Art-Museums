package com.harvard.art.museums.domain

import com.harvard.art.museums.data.HelloWorldRepository
import io.reactivex.Observable

/**
 * In a Production app, inject your Use Case into your Presenter instead.
 */
object GetHelloWorldTextUseCase {
    fun getHelloWorldText(): Observable<HelloWorldViewState> {
        return HelloWorldRepository.loadHelloWorldText()
                .map<HelloWorldViewState> { HelloWorldViewState.DataState(it) }
                .startWith(HelloWorldViewState.LoadingState)
                .onErrorReturn { HelloWorldViewState.ErrorState(it) }
    }
}


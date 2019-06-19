package com.harvard.art.museums.ext

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit


class ListExtKtTest {


    @Test
    fun testObservalble() {

        val disposable = CompositeDisposable()

        val scheduler = TestScheduler()

        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        val share = Observable.fromIterable(list).share()
        val oddEvent = share.filter { it % 2 == 0 }.map { " ood $it" }.subscribeOn(scheduler)
        val evenEvent = share.filter { it % 2 != 0 }.map { " even $it" }.subscribeOn(scheduler)

        Observable.merge<String>(
                oddEvent, evenEvent
        ).flatMap { Observable.just(it) }
                .subscribe { println(" ood $it") }
                .also { disposable.add(it) }


//        disposable.add(oddEvent.subscribe { println(" ood $it") })
//        disposable.add(evenEvent.subscribe { println(" even $it") })


//                .concatMap { squareOf(it) }
//                .onErrorResumeNext(ObservableSource { 5 })
//                .subscribe(
//                        { println("NEXT >> " + Thread.currentThread().name + "  " + it) },
//                        { println("ERROR " + it.message) },
//                        { println("COMPLETE") }
//                )
//
        scheduler.advanceTimeBy(11, TimeUnit.MINUTES)

    }


    fun squareOf(number: Int): Observable<Int> {

        if (number == 5)
            throw Exception("error")

        return Observable.just(number * number)//.subscribeOn(Schedulers.io())
    }


}
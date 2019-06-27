package com.harvard.art.museums.ext

import io.reactivex.*
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit


class ListExtKtTest {
    val testScheduler = TestScheduler()

    @Before
    fun setUp() {

        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    val transformer2 = object : ObservableTransformer<String, Int> {

        override fun apply(upstream: Observable<String>): ObservableSource<Int> {

            return upstream.flatMap { Observable.just(4) }

        }

    }/* any necessary params here *//* any necessary initialization here */


    val transformer = object : SingleTransformer<String, Int> {
        override fun apply(upstream: Single<String>): SingleSource<Int> {
            return upstream.flatMap { Single.just(1) }
        }
    }

    @Test
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    fun testObservalble() {

        val list = listOf("","", "")

        Observable.fromIterable(list).compose<Int>(transformer2)
                .subscribe(
                        { println(it) },
                        {},
                        {}
                )


//        Observable.interval(5,2, TimeUnit.SECONDS, Schedulers.computation())
//                .take(5)
//                .doOnNext{print(it)}
//                .map { 2 }
//                .subscribe { print("") }


//        Observable
//                .interval(10, TimeUnit.SECONDS)
//
//                .take(5)
//                .flatMap { Observable.just(it) }
//                .debounce(20, TimeUnit.SECONDS)
//                .subscribe(
//                        {
//                            println(it)
//                        },
//                        {
//                            print("--- err")
//                        }
//                )
//
//        testScheduler.advanceTimeTo(200, TimeUnit.SECONDS)


    }


}
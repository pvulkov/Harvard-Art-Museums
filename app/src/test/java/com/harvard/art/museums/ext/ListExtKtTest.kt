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
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    fun testObservalble() {

        val list = listOf(1, 2, 3, 4, 5, 6)


        Observable.fromIterable(list)
                .flatMap { ft1(it) }
                .onErrorResumeNext (Observable.fromIterable(list) )
//                .onErrorReturn { 8  }
//                .onErrorReturnItem(8)
                .subscribe(
                        { println(it) },
                        { println("ERROR") },
                        { println("COMPLETE") }
                )


    }

    private fun ft1(i: Int) = Observable.just(i)
            .map {

                if (i == 2)
                    throw Exception("ouch")

                i * i
            }
          //  .onErrorReturnItem(8)

}
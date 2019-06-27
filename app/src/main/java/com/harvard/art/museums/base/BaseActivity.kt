package com.harvard.art.museums.base

import android.content.Context
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

/**
 * BaseActivity providing required methods and presenter instantiation and calls.
 * @param P the viewType of the presenter the Activity is based on
 */
abstract class BaseActivity<V : MvpView, P : MviPresenter<V, *>> : MviActivity<V, P>(), BaseView {

    protected lateinit var presenter: P

    protected val disposable by lazy { CompositeDisposable() }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        presenter = instantiatePresenter()
//    }


    /**
     * Instantiates the presenter the Activity is based on.
     */
//    protected abstract fun instantiatePresenter(): P

    override fun getContext(): Context {
        return this
    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed)
            disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


}
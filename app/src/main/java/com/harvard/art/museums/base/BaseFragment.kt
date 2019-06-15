package com.harvard.art.museums.base

import android.content.Context
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

/**
 * BaseFragment providing required methods and presenter instantiation and calls.
 * @param P the type of the presenter the Fragment is based on
 */
abstract class BaseFragment<V : MvpView, P : MviPresenter<V, *>> : MviFragment<V, P>(), BaseView {


    protected val disposable by lazy { CompositeDisposable() }

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

    override fun getContext(): Context = this.activity as Context


}
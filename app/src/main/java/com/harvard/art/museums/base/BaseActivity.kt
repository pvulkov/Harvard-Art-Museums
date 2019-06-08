package com.harvard.art.museums.base

import android.content.Context
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * BaseActivity providing required methods and presenter instantiation and calls.
 * @param P the type of the presenter the Activity is based on
 */
abstract class BaseActivity<V : MvpView, P : MviPresenter<V, *>> : MviActivity<V, P>(), BaseView {

    protected lateinit var presenter: P

    private var creationTimeDelta = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creationTimeDelta = System.currentTimeMillis()
//        presenter = instantiatePresenter()
    }


    /**
     * Instantiates the presenter the Activity is based on.
     */
//    protected abstract fun instantiatePresenter(): P

    override fun getContext(): Context {
        return this
    }


}
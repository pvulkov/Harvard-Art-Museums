package com.harvard.art.museums.base

import android.content.Context
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * BaseFragment providing required methods and presenter instantiation and calls.
 * @param P the type of the presenter the Fragment is based on
 */
abstract class BaseFragment<V : MvpView, P : MviPresenter<V, *>> : MviFragment<V, P>(), BaseView {

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

    override fun getContext(): Context =  this.activity as Context


}
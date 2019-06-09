package com.harvard.art.museums.base

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.harvard.art.museums.injection.component.DaggerPresenterInjector
import com.harvard.art.museums.injection.component.PresenterInjector
import com.harvard.art.museums.injection.module.ContextModule
import com.harvard.art.museums.injection.module.NetworkModule
import com.harvard.art.museums.network.HamApi
import javax.inject.Inject

/**
 * Base presenter any presenter of the application must extend. It provides initial injections and
 * required methods.
 */


abstract class BasePresenter<V : BaseView, VS>(protected val view: V) : MviBasePresenter<V, VS>() {

    private val injector: PresenterInjector = DaggerPresenterInjector
            .builder()
            .baseView(view)
            .contextModule(ContextModule)
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    @Inject
    protected lateinit var hamApi: HamApi


    open fun onViewCreated() {
    }

    /**
     * Called when the presenter view is destroyed
     */
    open fun onViewDestroyed() {}


    private fun inject() {
        when (this) {
//            is RecipesListPresenter -> injector.inject(this)
//            is RecipeDetailsPresenter -> injector.inject(this)
        }
    }
}
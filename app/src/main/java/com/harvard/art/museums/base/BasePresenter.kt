package com.harvard.art.museums.base

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.harvard.art.museums.data.db.HamDatabase
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsPresenter
import com.harvard.art.museums.injection.component.DaggerPresenterInjector
import com.harvard.art.museums.injection.component.PresenterInjector
import com.harvard.art.museums.injection.module.ContextModule
import com.harvard.art.museums.injection.module.NetworkModule
import com.harvard.art.museums.data.network.HamApi
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsPresenter
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsPresenter
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter
import com.harvard.art.museums.features.objects.list.ObjectsPresenter
import com.harvard.art.museums.features.search.SearchPresenter
import com.harvard.art.museums.injection.module.DatabaseModule
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
            .databaseModule(DatabaseModule)
            .build()

    init {
        inject()
    }

    @Inject
    protected lateinit var hamApi: HamApi

    @Inject
    protected lateinit var hamDb: HamDatabase

    //TODO (pvalkov) check if thta is used
    open fun onViewCreated() {
    }

    /**
     * Called when the presenter view is destroyed
     */
    open fun onViewDestroyed() {}


    private fun inject() {
        when (this) {
            is ExhibitionsPresenter -> injector.inject(this)
            is ExhibitionGalleryPresenter -> injector.inject(this)
            is GalleryDetailsPresenter -> injector.inject(this)
            is SearchPresenter -> injector.inject(this)
            is ExhibitionDetailsPresenter -> injector.inject(this)
            is ObjectsPresenter -> injector.inject(this)
            is ObjectDetailsPresenter -> injector.inject(this)
        }
    }
}
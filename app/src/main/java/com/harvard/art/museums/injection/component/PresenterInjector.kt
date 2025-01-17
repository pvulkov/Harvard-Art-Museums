package com.harvard.art.museums.injection.component

import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsPresenter
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsPresenter
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsPresenter
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter
import com.harvard.art.museums.features.objects.list.ObjectsPresenter
import com.harvard.art.museums.features.search.SearchPresenter
import com.harvard.art.museums.injection.module.ContextModule
import com.harvard.art.museums.injection.module.DatabaseModule
import com.harvard.art.museums.injection.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [
    ContextModule::class,
    NetworkModule::class,
    DatabaseModule::class]
)
interface PresenterInjector {

    /**
     * Injects required dependencies into the specified Presenter.
     * @param presenter Presenter into which to inject the dependencies
     */
    fun inject(presenter: ExhibitionsPresenter)

    fun inject(presenter: ExhibitionGalleryPresenter)

    fun inject(presenter: GalleryDetailsPresenter)

    fun inject(presenter: SearchPresenter)

    fun inject(presenter: ExhibitionDetailsPresenter)

    fun inject(presenter: ObjectsPresenter)

    fun inject(presenter: ObjectDetailsPresenter)


    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}
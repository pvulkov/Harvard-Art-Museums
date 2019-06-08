package com.harvard.art.museums.injection.component

import com.harvard.art.museums.base.BaseView
import com.harvard.art.museums.features.main.HelloWorldPresenter
import com.harvard.art.museums.injection.module.ContextModule
import com.harvard.art.museums.injection.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(ContextModule::class), (NetworkModule::class)])
interface PresenterInjector {

    /**
     * Injects required dependencies into the specified Presenter.
     * @param presenter Presenter into which to inject the dependencies
     */
    fun inject(presenter: HelloWorldPresenter)

//    fun inject(presenter: RecipeDetailsPresenter)


    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}
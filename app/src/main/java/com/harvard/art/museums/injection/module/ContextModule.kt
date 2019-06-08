package com.harvard.art.museums.injection.module

import android.content.Context
import com.harvard.art.museums.base.BaseView
import dagger.Module
import dagger.Provides


/**
 * Module providing Context dependencies
 */
@Module
@Suppress("unused")
object ContextModule {


    /**
     * Provides Context.
     * @param baseView the baseView providing the context
     * @return context
     */
    @Provides
    @JvmStatic
    internal fun provideContext(baseView: BaseView): Context {
        return baseView.getContext()
    }

}
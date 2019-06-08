package com.harvard.art.museums.base

import android.content.Context
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * Base view any view must implement.
 */
interface BaseView : MvpView {

    /**
     * @return the view Context
     */
    fun getContext(): Context

}
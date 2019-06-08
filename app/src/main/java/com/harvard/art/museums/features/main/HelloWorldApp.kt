package com.harvard.art.museums.features.main

import android.app.Application
import com.harvard.art.museums.BuildConfig
import timber.log.Timber

class HelloWorldApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}


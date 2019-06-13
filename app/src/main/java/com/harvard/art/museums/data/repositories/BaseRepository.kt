package com.harvard.art.museums.data.repositories


open class BaseRepository {


    protected fun handleErrorCallback(throwable: Throwable, errorCallback: ErrorCallback?) {
        //TODO (pvalkov) implemente crashlytics
//        if (Fabric.isInitialized())
//            Crashlytics.logException(throwable)

        errorCallback?.onError(throwable)
    }

    interface ErrorCallback {
        fun onError(throwable: Throwable)
    }

}
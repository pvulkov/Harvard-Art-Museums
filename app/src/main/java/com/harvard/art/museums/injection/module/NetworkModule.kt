package com.harvard.art.museums.injection.module

import android.content.Context
import com.harvard.art.museums.BASE_URL
import com.harvard.art.museums.BuildConfig
import com.harvard.art.museums.data.network.HamApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Module providing network dependencies
 */
@Module(includes = [(ContextModule::class)])
object NetworkModule {


    /**
     * Provides the HAM service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the HAM service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideHamApi(retrofit: Retrofit): HamApi {
        return retrofit.create(HamApi::class.java)
    }


    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }


    /**
     * Provides the okHttpClient object.
     *
     * NOTE (pvalkov) check link below for cache control
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control
     *
     * @return the okHttpClient object
     */
    @Provides
    @JvmStatic
    @Singleton
    internal fun provideOkHttpClient(context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                var request = chain.request()
//                request = request.newBuilder().header("Cache-Control", "public, max-age=$SESSION_CACHE_TIME").build()
//                chain.proceed(request)
//            }


        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })


        return httpClient.build()
    }

}
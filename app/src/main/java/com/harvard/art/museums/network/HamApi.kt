package com.harvard.art.museums.network

import com.harvard.art.museums.API_KEY
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface HamApi {


    //http://mobile.asosservices.com/sampleapifortest/recipes.ison
    @GET("{path}/{file}")
    fun getRecipes(
        @Path("path") rest: String = "sampleapifortest",
        @Path("apikey") apikey: String = API_KEY
    ): Observable<List<Any>>


}
package com.harvard.art.museums.network

import com.harvard.art.museums.API_KEY
import com.harvard.art.museums.data.pojo.Exhibitions
import io.reactivex.Observable
import retrofit2.http.*


interface HamApi {



    //https://iiif.harvardartmuseums.org/collections/top?apikey=4493ff90-89fa-11e9-bae4-390e251d4987
    //https://iiif.harvardartmuseums.org/collections/top
    @GET("collections/top")
    fun getTopLevelCollection(@Query("apikey") apikey: String = API_KEY): Observable<Any>


    //https://github.com/harvardartmuseums/api-docs/blob/master/sections/exhibition.md
    @GET("exhibition")
    fun getExhibitions(
        @Query("venue") venue: String = "HAM",
        @Query("apikey") apikey: String = API_KEY
    ): Observable<Exhibitions>


}
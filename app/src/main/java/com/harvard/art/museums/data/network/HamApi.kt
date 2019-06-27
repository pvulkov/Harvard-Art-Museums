package com.harvard.art.museums.data.network

import com.harvard.art.museums.API_KEY
import com.harvard.art.museums.data.pojo.Exhibition
import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.data.pojo.RecordsInfoData
import io.reactivex.Observable
import io.reactivex.Single
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
            @Query("sort") sort: String = "chronological",
            @Query("sortorder") sortorder: String = "desc",
            @Query("status") status: String = "current, upcoming, past",
            @Query("apikey") apikey: String = API_KEY
    ): Single<Exhibitions>


    @GET
    fun getNextExhibitionsPage(@Url url: String): Single<Exhibitions>


    //https://api.harvardartmuseums.org/exhibition?apikey=4493ff90-89fa-11e9-bae4-390e251d4987&q=woodblock
    @GET("exhibition")
    fun getExhibitionsByKeyword(
            @Query("q") q: String,
            @Query("sort") sort: String = "chronological",
            @Query("sortorder") sortorder: String = "desc",
            @Query("status") status: String = "current, upcoming, past",
            @Query("apikey") apikey: String = API_KEY
    ): Single<Exhibitions>

    //https://api.harvardartmuseums.org/object?apikey=4493ff90-89fa-11e9-bae4-390e251d4987&exhibition=5700&fields=images
    @GET("object")
    fun getExhibitionImages(
            @Query("exhibition") exhibitionId: Int,
            @Query("fields") fields: String = "images",
            @Query("apikey") apikey: String = API_KEY
    ): Single<RecordsInfoData>


    //    //https://api.harvardartmuseums.org/object?apikey=4493ff90-89fa-11e9-bae4-390e251d4987&exhibition=5700&fields=images
    @GET("exhibition/{exhibitionId}")
    fun getExhibitionDetails(
            @Path("exhibitionId") exhibitionId: Int,
            @Query("apikey") apikey: String = API_KEY
    ): Single<Exhibition>


    @GET
    fun getNextObjectsPage(@Url url: String): Single<RecordsInfoData>

    //https://api.harvardartmuseums.org/object?apikey=4493ff90-89fa-11e9-bae4-390e251d4987&exhibition=5700&fields=images
    @GET("object")
    fun searchObjectByKeyword(
            @Query("keyword") keyword: String,
            @Query("size") size: Int = 10,
            @Query("apikey") apikey: String = API_KEY
    ): Single<RecordsInfoData>


    //https://api.harvardartmuseums.org/object?apikey=4493ff90-89fa-11e9-bae4-390e251d4987&sortorder=desc&size=30
    @GET("object")
    fun getObjects(
            @Query("sort") sort: String = "rank",
            @Query("sortorder") sortorder: String = "asc",
            @Query("size") size: Int = 30,
            @Query("apikey") apikey: String = API_KEY
    ): Single<RecordsInfoData>


    //https://github.com/harvardartmuseums/api-docs/blob/master/sections/image.md
    //https://api.harvardartmuseums.org/object?apikey=4493ff90-89fa-11e9-bae4-390e251d4987&exhibition=5700&field:images&size=100
    @GET("exhibition")
    fun getImages(
            @Query("venue") venue: String = "HAM",
            @Query("sort") sort: String = "chronological",
            @Query("sortorder") sortorder: String = "desc",
            @Query("status") status: String = "upcoming,current,upcoming",
            @Query("apikey") apikey: String = API_KEY
    ): Observable<Exhibitions>


}
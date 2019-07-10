package com.harvard.art.museums.data.repositories

import com.harvard.art.museums.data.db.HamDatabase
import com.harvard.art.museums.data.network.HamApi
import com.harvard.art.museums.data.pojo.ExhibitionRecord
import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.ext.toExhibitionRecordsList
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class ExhibitionsRepository(private val hamApi: HamApi, private val database: HamDatabase) : BaseRepository() {


    fun getExhibitions(errorCallback: ErrorCallback? = null): Observable<List<ExhibitionRecord>> {
        return Observable.merge(
                getExhibitionsFromDbIfNotEmpty(),
                getExhibitionsFromApi(errorCallback)
        )
    }


    fun getNextExhibitions(url: String, errorCallback: ErrorCallback? = null): Observable<List<ExhibitionRecord>> {
        return Observable.merge(
                getExhibitionsFromDbIfNotEmpty(url),
                getNextExhibitionsFromApi(url, errorCallback)
        )
    }


    private fun getExhibitionsFromDbIfNotEmpty() =
            database.exhibitionRecordDao().fetchAll()
                    .filter { it.isNotEmpty() }
                    .toObservable()


    private fun getExhibitionsFromDbIfNotEmpty(url: String) =
            database.exhibitionRecordDao().fetchAll(url)
                    .filter { it.isNotEmpty() }
                    .toObservable()


    //NOTE (pvalkov) based on https://medium.com/@rikvanv/android-repository-pattern-using-room-retrofit2-and-rxjava2-b48aedd173c
    private fun getExhibitionsFromApi(errorCallback: ErrorCallback?): Observable<List<ExhibitionRecord>> {

        return getExhibitionsData()
                .flatMap { toExhibitionRecords(it) }
                .map { toSortedList(it) }
                .flatMap {
                    saveExhibitionRecords(it)
                }
                .toObservable()
                .materialize()
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    // if the observables onError is called, invoke callback so that presenters can handle error
                    it.error?.let {
                        handleErrorCallback(it, errorCallback)
                    }
                    // put itemObject back into stream
                    it
                }
                .filter { !it.isOnError }
                .dematerialize<List<ExhibitionRecord>>()
                .debounce(400, TimeUnit.MILLISECONDS)
    }


    //NOTE (pvalkov) based on https://medium.com/@rikvanv/android-repository-pattern-using-room-retrofit2-and-rxjava2-b48aedd173c
    private fun getNextExhibitionsFromApi(url: String, errorCallback: ErrorCallback?): Observable<List<ExhibitionRecord>> {

        return getNextExhibitionsData(url)
                .flatMap { toExhibitionRecords(it, url) }
                .map { toSortedList(it) }
                .flatMap {
                    saveExhibitionRecords(it)
                }
                .toObservable()
                .materialize()
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    // if the observables onError is called, invoke callback so that presenters can handle error
                    it.error?.let {
                        handleErrorCallback(it, errorCallback)
                    }
                    // put itemObject back into stream
                    it
                }
                .filter { !it.isOnError }
                .dematerialize<List<ExhibitionRecord>>()
                .debounce(400, TimeUnit.MILLISECONDS)
    }

    private fun toSortedList(recordsList: List<ExhibitionRecord>): List<ExhibitionRecord> {
        return recordsList.sortedBy { it.openStatus }
    }

    private fun toExhibitionRecords(exhibitions: Exhibitions, url: String = EMPTY) =
            Single.just(exhibitions.toExhibitionRecordsList(url))


    private fun getExhibitionsData() = hamApi.getExhibitions()


    private fun getNextExhibitionsData(url: String) = hamApi.getNextExhibitionsPage(url)


    private fun saveExhibitionRecords(recordsList: List<ExhibitionRecord>) =
            Completable.fromAction { database.exhibitionRecordDao().insert(recordsList) }
                    .toSingle { recordsList }

}
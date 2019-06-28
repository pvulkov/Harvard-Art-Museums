package com.harvard.art.museums.data.repositories

import com.harvard.art.museums.data.db.HamDatabase
import com.harvard.art.museums.data.network.HamApi
import com.harvard.art.museums.data.pojo.ObjectRecord
import com.harvard.art.museums.data.pojo.RecordsInfoData
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.ext.toObjectRecordItems
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class ObjectsRepository(private val hamApi: HamApi, private val database: HamDatabase) : BaseRepository() {


    fun getObjects(errorCallback: ErrorCallback? = null): Observable<List<ObjectRecord>> {
        return Observable.merge(
                getObjectsFromDbIfNotEmpty(),
                getObjectsFromApi(errorCallback)
        )
    }


    fun getNextObjects(url: String, errorCallback: ErrorCallback? = null): Observable<List<ObjectRecord>> {
        return Observable.merge(
                getObjectsFromDbIfNotEmpty(url),
                getNextObjectsFromApi(url, errorCallback)
        )
    }


    private fun getObjectsFromDbIfNotEmpty() =
            database.objectsRecordDao().fetchAll()
                    .filter { it.isNotEmpty() }
                    .toObservable()


    private fun getObjectsFromDbIfNotEmpty(url: String) =
            database.objectsRecordDao().fetchAll(url)
                    .filter { it.isNotEmpty() }
                    .toObservable()


    //NOTE (pvalkov) based on https://medium.com/@rikvanv/android-repository-pattern-using-room-retrofit2-and-rxjava2-b48aedd173c
    private fun getObjectsFromApi(errorCallback: ErrorCallback?): Observable<List<ObjectRecord>> {

        return getObjectsData()
                .flatMap { toObjectRecords(it) }
                //TODO (pvalkov) check if we need that
                // .map { toSortedList(it) }
                .flatMap {
                    saveObjectRecords(it)
                }
                .toObservable()
                .materialize()
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    // if the observables onError is called, invoke callback so that presenters can handle error
                    it.error?.let {
                        handleErrorCallback(it, errorCallback)
                    }
                    // put item back into stream
                    it
                }
                .filter { !it.isOnError }
                .dematerialize<List<ObjectRecord>>()
                .debounce(400, TimeUnit.MILLISECONDS)
    }


    //NOTE (pvalkov) based on https://medium.com/@rikvanv/android-repository-pattern-using-room-retrofit2-and-rxjava2-b48aedd173c
    private fun getNextObjectsFromApi(url: String, errorCallback: ErrorCallback?): Observable<List<ObjectRecord>> {

        return getNextObjectsData(url)
                .flatMap { toObjectRecords(it) }

                //TODO (pvalkov) check if we need that
                // .map { toSortedList(it) }
//                .flatMap {
//                    saveObjectRecords(it)
//                }
                .toObservable()
//                .materialize()
//                .observeOn(AndroidSchedulers.mainThread())
//                .map {
//                    // if the observables onError is called, invoke callback so that presenters can handle error
//                    it.error?.let {
//                        handleErrorCallback(it, errorCallback)
//                    }
//                    // put item back into stream
//                    it
//                }
//                .filter { !it.isOnError }
//                .dematerialize<List<ObjectRecord>>()
                .debounce(400, TimeUnit.MILLISECONDS)
    }


//    private fun toSortedList(recordsList: List<ExhibitionRecord>): List<ExhibitionRecord> {
//        return recordsList.sortedBy { it.openStatus }
//    }

    private fun toObjectRecords(data: RecordsInfoData) =
            Single.just(data.toObjectRecordItems(data.info.next ?: EMPTY))


    private fun getObjectsData() = hamApi.getObjects()


    private fun getNextObjectsData(url: String) = hamApi.getNextObjectsPage(url)


    private fun saveObjectRecords(recordsList: List<ObjectRecord>) =
            Completable.fromAction { database.objectsRecordDao().insert(recordsList) }
                    .toSingle { recordsList }

}
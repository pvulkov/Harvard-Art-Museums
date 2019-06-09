package com.harvard.art.museums.features.main

import com.harvard.art.museums.data.pojo.Exhibitions
import com.harvard.art.museums.network.HamApi
import io.reactivex.Observable

/**
 * In a Production app, inject your Use Case into your Presenter instead.
 */
object ExhibitionsUseCase {
    fun getHelloWorldText(hamApi: HamApi): Observable<ExhibitionsViewState> {
        return getCollection(hamApi)
                .map { toExhibitionItems(it) }
                .map<ExhibitionsViewState> { ExhibitionsViewState.DataState(it) }
                .startWith(ExhibitionsViewState.LoadingState)
                .onErrorReturn { ExhibitionsViewState.ErrorState(it) }
    }

    private fun getCollection(hamApi: HamApi) = hamApi.getExhibitions()


    private fun toExhibitionItems(exhibitions: Exhibitions): List<ExhibitionViewItem> {
        val viewItems = mutableListOf<ExhibitionViewItem>()

        exhibitions.records.forEach {
            viewItems.add(ExhibitionViewItem(it.title))
        }

        return viewItems
    }
}


package com.harvard.art.museums.features.search

import android.os.Bundle
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import kotlinx.android.synthetic.main.activity_exhibitions_details.*
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter.ExhibitionsDetailsView as ExdView
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryViewState as ExdViewState
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter as ExdPresenter
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryViewState.State.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import com.harvard.art.museums.features.search.SearchPresenter.SearchView


class SearchActivity : BaseActivity<SearchView, SearchPresenter>(), SearchView {

    private val trigger: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
    }


    override fun onResume() {
        super.onResume()
        trigger.onNext(true)
    }

    override fun createPresenter() = SearchPresenter(this)

//    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

//    override fun loadMoreEvent(): Observable<ViewItemAction> {
//        return adapter.loadMoreEvent()
//    }

    override fun render(state: SearchViewState) {
        when (state.state) {
            INIT -> Unit
            LOAD_MORE -> Unit
            DATA -> renderDataState(state)
            ERROR -> Unit
        }
    }


    private fun renderDataState(state: SearchViewState) {
       // adapter.updateData(state.exhibitionsList)

    }

    private fun renderErrorState(state: SearchViewState) {
        Toast.makeText(this, "error ${state.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
//        setSupportActionBar(toolbar)

      //  pagerContainer.adapter = adapter

    }
}

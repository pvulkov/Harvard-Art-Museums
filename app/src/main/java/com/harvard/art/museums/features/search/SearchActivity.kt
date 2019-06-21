package com.harvard.art.museums.features.search

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.setColor
import com.harvard.art.museums.features.search.Action.FILTER
import com.harvard.art.museums.features.search.Action.SEARCH
import com.harvard.art.museums.features.search.Filter.*
import com.harvard.art.museums.features.search.SearchPresenter.SearchView
import com.harvard.art.museums.features.search.SearchViewState.State.DATA
import com.harvard.art.museums.features.search.SearchViewState.State.ERROR
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit


class SearchActivity : BaseActivity<SearchView, SearchPresenter>(), SearchView {

    private val trigger: PublishSubject<SearchViewAction> = PublishSubject.create<SearchViewAction>()
    private val adapter = SearchAdapter()
    private var filter: Filter = EXHIBITION


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        filter = intent.getSerializableExtra("filter.type") as Filter

        initUI()
    }


    override fun onResume() {
        super.onResume()
        trigger.onNext(SearchViewAction(exhFilterView, FILTER, filter))
    }


    override fun filterEvent(): Observable<SearchViewAction> = trigger.filter { it.action == FILTER }

    override fun searchEvent(): Observable<SearchViewAction> = trigger.filter { it.action == SEARCH }


    override fun createPresenter() = SearchPresenter(this)

//    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

    override fun render(state: SearchViewState) {
        when (state.state) {
//            INIT -> Unit
            SearchViewState.State.FILTER -> renderChangeFilterState(state.filter)
            DATA -> renderDataState(state)
            ERROR -> Unit
        }
    }


    private fun renderChangeFilterState(filter: Filter) {

        val blueColor = ContextCompat.getColor(this, R.color.blue)
        val grayColor = ContextCompat.getColor(this, R.color.light_gray)


        when (filter) {
            EXHIBITION -> {
                exhFilterView.setColor(blueColor)
                exhObjectView.setColor(grayColor)
                exhUnknownView.setColor(grayColor)

                exhFilterViewText.setTextColor(blueColor)
                exhObjectViewText.setTextColor(grayColor)
                exhUnknownViewText.setTextColor(grayColor)
            }
            OBJECTS -> {
                exhFilterView.setColor(grayColor)
                exhObjectView.setColor(blueColor)
                exhUnknownView.setColor(grayColor)

                exhFilterViewText.setTextColor(grayColor)
                exhObjectViewText.setTextColor(blueColor)
                exhUnknownViewText.setTextColor(grayColor)
            }
            UNKNOWN -> {
                exhFilterView.setColor(grayColor)
                exhObjectView.setColor(grayColor)
                exhUnknownView.setColor(blueColor)

                exhFilterViewText.setTextColor(grayColor)
                exhObjectViewText.setTextColor(grayColor)
                exhUnknownViewText.setTextColor(blueColor)
            }
        }


    }

    private fun renderDataState(state: SearchViewState) {

        adapter.updateData(state.items)
        // adapter.updateData(state.exhibitionsList)

    }

    private fun renderErrorState(state: SearchViewState) {
        Toast.makeText(this, "error ${state.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {

        searchResultsView.let {
            val manager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            setSpanLookUp(manager)
            it.layoutManager = manager
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        }



        Observable.merge(
                exhFilterView.clicks().map { SearchViewAction(exhFilterView, FILTER, EXHIBITION) },
                exhObjectView.clicks().map { SearchViewAction(exhObjectView, FILTER, OBJECTS) },
                exhUnknownView.clicks().map { SearchViewAction(exhObjectView, FILTER, UNKNOWN) }
        ).throttleLatest(200, TimeUnit.MILLISECONDS)
                .subscribe(trigger)

        RxSearchView.queryTextChangeEvents(searchTextView)
                .map { it.queryText().toString() }
                .filter { it.length > 1 }
                .map { SearchViewAction(exhObjectView, SEARCH, UNKNOWN, it) }
                .debounce(300, TimeUnit.MILLISECONDS).subscribe(trigger)
    }

    private fun setSpanLookUp(manager: GridLayoutManager) {

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = adapter.getItemSpan(position)
        }
    }
}

package com.harvard.art.museums.features.search

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit


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


    override fun filterEvent(): Observable<SearchViewAction> {

        return Observable.merge(
                exhFilterView.clicks().map { SearchViewAction(exhFilterView, FILTER, EXHIBITION) },
                exhObjectView.clicks().map { SearchViewAction(exhObjectView, FILTER, OBJECTS) },
                exhUnknownView.clicks().map { SearchViewAction(exhObjectView, FILTER, UNKNOWN) }
        ).throttleLatest(200, TimeUnit.MILLISECONDS)
    }

    override fun searchEvent(): Observable<SearchViewAction> {
        return RxTextView.textChanges(searchTextView)
                .map { searchTextView.text.toString() }
                .filter { it.length > 1 }
                .map { SearchViewAction(exhObjectView, SEARCH, UNKNOWN, it) }
                .debounce(300, TimeUnit.MILLISECONDS)
    }


    override fun createPresenter() = SearchPresenter(this)

//    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

    override fun render(state: SearchViewState) {
        when (state.state) {
//            INIT -> Unit
            SearchViewState.State.FILTER -> renderChangeFilterState(state)
            DATA -> renderDataState(state)
            ERROR -> Unit
        }
    }


    private fun renderChangeFilterState(state: SearchViewState) {
        Log.d("DEBUG", "state " + state.state)

        val blueColor = getColor(R.color.blue)
        val grayColor = getColor(R.color.light_gray)


        when (state.filter) {
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

        adapter.updateData(state.data)
        // adapter.updateData(state.exhibitionsList)

    }

    private fun renderErrorState(state: SearchViewState) {
        Toast.makeText(this, "error ${state.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {

        searchView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        }


//        exhFilterView.setColor(getColor(R.color.light_gray))
//        exhObjectView.setColor(getColor(R.color.light_gray))
//        exhUnknownView.setColor(getColor(R.color.light_gray))
    }
}

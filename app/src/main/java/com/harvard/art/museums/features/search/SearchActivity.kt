package com.harvard.art.museums.features.search

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryActivity
import com.harvard.art.museums.features.search.Action.*
import com.harvard.art.museums.features.search.Filter.*
import com.harvard.art.museums.features.search.SearchPresenter.SearchView
import com.harvard.art.museums.features.search.SearchViewState.State.*
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView

import com.harvard.art.museums.features.search.SearchViewState as ViewState

import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit


class SearchActivity : BaseActivity<SearchView, SearchPresenter>(), SearchView {

    private val trigger: PublishSubject<SearchViewAction> = PublishSubject.create<SearchViewAction>()
    private val adapter = SearchAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
    }


    override fun onResume() {
        super.onResume()
        val filter = intent.getSerializableExtra("filter.type") as Filter
        trigger.onNext(SearchViewAction(ACTION_INIT, filter))
        trigger.onNext(SearchViewAction(ACTION_FILTER, filter))
    }

    override fun createPresenter() = SearchPresenter(this)

    override fun itemActionsEvents(): Observable<SearchResultViewItem> = adapter.viewEvents()

    override fun initEvent(): Observable<SearchViewAction> = trigger.filter { it.action == ACTION_INIT }

    override fun filterEvent(): Observable<SearchViewAction> = trigger.filter { it.action == ACTION_FILTER }

    override fun searchEvent(): Observable<SearchViewAction> = trigger.filter { it.action == ACTION_SEARCH }


    override fun render(state: ViewState) {
        when (state.state) {
            INIT -> renderInitState(state)
            SEARCHING -> renderSearchingState()
            REPEAT_SEARCH -> renderRepeatSearchState(state)
            CHANGE_FILTER -> renderChangeFilterState(state.filter)
            DATA -> renderDataState(state)
            ERROR -> renderErrorState(state)
            OPEN_ITEM -> renderOpenItemState(state)
        }
    }

    private fun renderInitState(state: ViewState) {
        applyFilter(state.filter)
        filterCardView.show()
        resultsCardView.show()
        adapter.updateData(state.items)
    }

    private fun renderSearchingState() {
        progressView.show()
        filterCardView.hide()
        resultsCardView.hide()
    }

    private fun renderDataState(state: ViewState) {
        progressView.hide()
        filterCardView.hide()
        resultsCardView.show()
        adapter.updateData(state.items)
    }

    private fun renderErrorState(state: ViewState) {
        progressView.hide()
        Toast.makeText(this, "error ${state.error}", Toast.LENGTH_LONG).show()
    }

    private fun renderRepeatSearchState(state: ViewState) {
        state.text?.let { searchTextView.setQuery(it, true) }
    }

    private fun renderOpenItemState(state: ViewState) {
        val extras = Bundle()
        extras.putInt("exhibitionId", state.exhibitionId!!)
        generateActivityIntent(ExhibitionGalleryActivity::class.java, extras)
                .also { startActivity(it) }

    }


    private fun renderChangeFilterState(filter: Filter) {
        applyFilter(filter)
    }


    private fun applyFilter(filter: Filter) {
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


    private fun initUI() {

        searchResultsView.let {
            val manager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            setSpanLookUp(manager)
            it.layoutManager = manager
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
        }



        Observable.merge(
                exhFilterView.clicks().map { SearchViewAction(ACTION_FILTER, EXHIBITION) },
                exhObjectView.clicks().map { SearchViewAction(ACTION_FILTER, OBJECTS) },
                exhUnknownView.clicks().map { SearchViewAction(ACTION_FILTER, UNKNOWN) }
        )
                .throttleLatest(200, TimeUnit.MILLISECONDS)
                .subscribe(trigger)



        RxSearchView.queryTextChangeEvents(searchTextView)
                .doOnNext {
                    if (it.isSubmitted)
                        hideSoftKeyboard()
                }
                .map { SearchViewAction(ACTION_SEARCH,  UNKNOWN, it.queryText().toString(), it.isSubmitted) }
                //TODO (pvalkov) ig text is empty do not debounce
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(trigger)
    }


    private fun setSpanLookUp(manager: GridLayoutManager) {
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = adapter.getItemSpan(position)
        }
    }
}

package com.harvard.art.museums.features.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.replaceFragment
import com.harvard.art.museums.features.exhibitions.list.ExhibitionsFragment
import com.harvard.art.museums.features.home.HomePresenter.HomeView
import com.harvard.art.museums.features.home.HomeViewState.State.*
import com.harvard.art.museums.features.home.HomeViewState as ViewState
import com.harvard.art.museums.features.home.data.NavigationAction
import com.harvard.art.museums.features.objects.list.ObjectsFragment
import com.harvard.art.museums.features.search.Filter
import com.harvard.art.museums.features.search.SearchActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header_layout.*
import java.lang.Exception
import java.util.concurrent.TimeUnit


class HomeActivity : BaseActivity<HomeView, HomePresenter>(), HomeView {

    private var filter: Filter = Filter.EXHIBITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initUI()
    }


    override fun createPresenter() = HomePresenter(this)

    override fun navigationEvent() =
            Observable.merge(
                    menuObjects.clicks().map { NavigationAction.OBJECTS },
                    menuExhibitions.clicks().map { NavigationAction.EXHIBITIONS }

            ).debounce(400, TimeUnit.MILLISECONDS)


    override fun render(state: ViewState) {
        when (state.state) {
            INIT, NAVIGATION -> renderNavigationState(state)
            ERROR -> renderErrorState(state)
        }
    }


    private fun renderNavigationState(state: ViewState) {
        Log.d("DEBUG", "renderNavigationState " + supportFragmentManager.fragments.size)
        replaceFragment(R.id.mainContainer, state.data!!)
        updateFilterValue(state)
    }


    private fun updateFilterValue(state: ViewState) {
        filter = when (state.data) {
            is ObjectsFragment -> Filter.OBJECTS
            is ExhibitionsFragment -> Filter.EXHIBITION
            else -> throw  Exception("Unhandled else case")

        }
    }

    private fun renderErrorState(errorState: ViewState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
//        setSupportActionBar(toolbar)


        //TODO (pvalkov) refactor code
        searchTextView.clicks()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe {
                    val intent = Intent(this@HomeActivity, SearchActivity::class.java)
                    intent.putExtra("filter.viewType", filter)
                    startActivity(intent)
                }
    }
}
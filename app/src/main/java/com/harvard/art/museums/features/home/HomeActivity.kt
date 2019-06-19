package com.harvard.art.museums.features.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.replaceFragment
import com.harvard.art.museums.features.home.HomePresenter.HomeView
import com.harvard.art.museums.features.home.HomeViewState.State.*
import com.harvard.art.museums.features.home.data.NavigationAction
import com.harvard.art.museums.features.search.SearchActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header_layout.*
import java.util.concurrent.TimeUnit


class HomeActivity : BaseActivity<HomeView, HomePresenter>(), HomeView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initUI()
    }


    override fun createPresenter() = HomePresenter(this)

    override fun navigationEvent() =
            Observable.merge(
                    menuObjects.clicks().flatMap { Observable.just(NavigationAction.OBJECTS) },
                    menuExhibitions.clicks().flatMap { Observable.just(NavigationAction.EXHIBITIONS) }

            ).throttleLatest(400, TimeUnit.MILLISECONDS)


    override fun render(state: HomeViewState) {
        when (state.state) {
            INIT, NAVIGATION -> renderNavigationState(state)
            ERROR -> renderErrorState(state)
        }
    }


    private fun renderNavigationState(state: HomeViewState) {
        Log.d("DEBUG", "renderNavigationState " + supportFragmentManager.fragments.size)
        replaceFragment(R.id.mainContainer, state.data!!)
    }

    private fun renderErrorState(errorState: HomeViewState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
//        setSupportActionBar(toolbar)


        //TODO (pvalkov) refactor code
        searchTextView.clicks().subscribe {
            val intent = Intent(this@HomeActivity, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}
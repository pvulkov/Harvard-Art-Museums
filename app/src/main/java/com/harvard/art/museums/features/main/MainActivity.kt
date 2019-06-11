package com.harvard.art.museums.features.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.features.main.MainPresenter.MainView
import com.harvard.art.museums.features.main.data.NavigationAction
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.main.MainViewState.State.*


class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    override fun createPresenter() = MainPresenter(this)

    override fun navigationEvent() = Observable.merge(
            menuObjects.clicks().flatMap { Observable.just(NavigationAction.OBJECTS) },
            menuExhibitions.clicks().flatMap { Observable.just(NavigationAction.EXHIBITIONS) })
            .throttleLatest(400, TimeUnit.MILLISECONDS)


    override fun render(state: MainViewState) {
        when (state.state) {
            INIT, NAVIGATION -> renderNavigationState(state)
            ERROR -> renderErrorState(state)
        }
    }


    private fun renderNavigationState(state: MainViewState) {

        Log.d("DEBUG", "renderNavigationState " + supportFragmentManager.fragments.size)

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (supportFragmentManager.fragments.isNotEmpty()) {
            fragmentTransaction.replace(R.id.mainContainer, state.data!!)
        } else {
            fragmentTransaction.add(R.id.mainContainer, state.data!!)
        }

        fragmentTransaction.addToBackStack(state.tag)
        fragmentTransaction.commit()
    }

    private fun renderErrorState(errorState: MainViewState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
        setSupportActionBar(toolbar)
    }
}

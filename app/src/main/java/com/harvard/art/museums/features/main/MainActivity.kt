package com.harvard.art.museums.features.main

import android.os.Bundle
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.visible
import com.harvard.art.museums.features.exhibitions.ExhibitionsActivity
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import com.harvard.art.museums.features.main.MainPresenter.MainView as MainView
import com.harvard.art.museums.features.main.MainViewState.*


class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setHasOptionsMenu(true)
        setContentView(R.layout.activity_main)
        initUI()

//        bottomNavigation.replaceMenu(R.menu.bottom_nav_menu)
    }

    override fun createPresenter() = MainPresenter(this)


    override fun render(state: MainViewState) {
        when (state) {
            is LoadingState -> renderLoadingState()
            is DataState -> renderDataState(state)
            is ErrorState -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
    }

    private fun renderDataState(state: DataState) {
    }

    private fun renderErrorState(errorState: ErrorState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {

        setSupportActionBar(toolbar)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = ExhibitionsActivity()
        fragmentTransaction.add(R.id.mainContainer, fragment)
        fragmentTransaction.commit()
    }
}

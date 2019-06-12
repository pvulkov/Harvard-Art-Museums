package com.harvard.art.museums.features.exhibitions.details

import android.os.Bundle
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.features.home.data.NavigationAction
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.exhibitions.details.ExhibitionsDetailsViewState as ExdViewState
import com.harvard.art.museums.features.exhibitions.details.ExhibitionsDetailsPresenter.ExhibitionsDetailsView as  ExdView


class ExhibitionsDetailsActivity : BaseActivity<ExdView, ExhibitionsDetailsPresenter>(), ExdView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibitions_details)
        initUI()
    }

    override fun createPresenter() = ExhibitionsDetailsPresenter(this)

    override fun navigationEvent() = Observable.merge(
            menuObjects.clicks().flatMap { Observable.just(NavigationAction.OBJECTS) },
            menuExhibitions.clicks().flatMap { Observable.just(NavigationAction.EXHIBITIONS) })
            .throttleLatest(400, TimeUnit.MILLISECONDS)


    override fun render(state: ExdViewState) {
        when (state.state) {
//            INIT, NAVIGATION -> renderNavigationState(state)
//            ERROR -> renderErrorState(state)
        }
    }


    private fun renderNavigationState(state: ExdViewState) {


    }

    private fun renderErrorState(errorState: ExdViewState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
        setSupportActionBar(toolbar)
    }
}

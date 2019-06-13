package com.harvard.art.museums.features.exhibitions.details

import android.os.Bundle
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import kotlinx.android.synthetic.main.activity_exhibitions_details.*
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsPresenter.ExhibitionsDetailsView as ExdView
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsViewState as ExdViewState
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsPresenter as ExdPresenter
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsViewState.State.*


class ExhibitionDetailsActivity : BaseActivity<ExdView, ExdPresenter>(), ExdView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibitions_details)
        initUI()
    }

    override fun createPresenter() = ExdPresenter(this)


    override fun render(state: ExdViewState) {
        when (state.state) {
            INIT -> Unit
            LOAD_MORE -> Unit
            DATA -> Unit
            ERROR -> Unit
        }
    }


    private fun renderNavigationState(state: ExdViewState) {


    }

    private fun renderErrorState(errorState: ExdViewState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
        setSupportActionBar(toolbar)

        pagerContainer.adapter = ExPagerAdapter(supportFragmentManager)

    }
}

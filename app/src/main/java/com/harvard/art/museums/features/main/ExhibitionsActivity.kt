package com.harvard.art.museums.features.main

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.visible
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_exibitions.*
import com.harvard.art.museums.features.main.ExhibitionsPresenter.ExhibitionsView as ExhView
import com.harvard.art.museums.features.main.ExhibitionsViewState.*


class ExhibitionsActivity : BaseActivity<ExhView, ExhibitionsPresenter>(), ExhView {

    private lateinit var exhibitionsAdapter: ExhibitionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exibitions)
        initUI()
    }

    override fun createPresenter() = ExhibitionsPresenter(this)

    override fun sayHelloWorldIntent() = helloWorldButton.clicks()

    override fun render(state: ExhibitionsViewState) {
        when (state) {
            is LoadingState -> renderLoadingState()
            is DataState -> renderDataState(state)
            is ErrorState -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
        loadingIndicator.visible = true
        helloWorldTextview.visible = false
    }

    private fun renderDataState(state: DataState) {
        loadingIndicator.visible = false
        exhibitionsAdapter.updateData(state.exhibitionItems)
    }

    private fun renderErrorState(errorState: ErrorState) {
        loadingIndicator.visible = false
        helloWorldTextview.visible = false
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
        exhibitionsAdapter = ExhibitionsAdapter()
        exhibitionsView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = exhibitionsAdapter
            it.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        }
    }
}

package com.harvard.art.museums.features.exhibitions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseFragment
import com.harvard.art.museums.ext.visible
import com.harvard.art.museums.features.exhibitions.ExhibitionsViewState.*
import com.harvard.art.museums.features.exhibitions.ExhibitionsViewState.State.*

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_exibitions.*
import com.harvard.art.museums.features.exhibitions.ExhibitionsPresenter.ExhibitionsView as ExhView


class ExhibitionsActivity : BaseFragment<ExhView, ExhibitionsPresenter>(), ExhView {

    private val trigger: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    private val exhibitionsAdapter = ExhibitionsAdapter()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_exibitions)
//        initUI()
//
//    }

    override fun onResume() {
        super.onResume()
        trigger.onNext(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_exibitions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = super.onViewCreated(view, savedInstanceState).also { initUI() }

    override fun createPresenter() = ExhibitionsPresenter(this)

    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

    override fun loadMoreEvent(): Observable<ExhibitionViewItem> {
        return exhibitionsAdapter.loadMoreEvent()
    }

    override fun render(state: ExhibitionsViewState) {
        when (state.state) {
            LOADING -> renderLoadingState()
            DATA -> renderDataState(state)
            ERROR -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
//        loadingIndicator.visible = true
//        helloWorldTextview.visible = false
    }

    private fun renderDataState(state: ExhibitionsViewState) {
//        loadingIndicator.visible = false
        exhibitionsAdapter.updateData(state.exhibitionItems)
    }

    private fun renderErrorState(state: ExhibitionsViewState) {
        Log.d("DEBUG", "error")
//        loadingIndicator.visible = false
//        helloWorldTextview.visible = false
        //Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
        exhibitionsView.let {
            it.layoutManager = LinearLayoutManager(this.context)
            it.adapter = exhibitionsAdapter
            it.addItemDecoration(DividerItemDecoration(this.context, RecyclerView.HORIZONTAL))
        }
    }
}

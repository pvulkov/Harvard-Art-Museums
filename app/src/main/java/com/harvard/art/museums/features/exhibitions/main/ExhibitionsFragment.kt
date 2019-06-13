package com.harvard.art.museums.features.exhibitions.main

import android.content.Intent
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
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.exhibitions.main.ExhibitionsViewState.State.*
import com.harvard.art.museums.features.exhibitions.data.ViewAction
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import com.harvard.art.museums.features.exhibitions.details.ExhibitionDetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_exibitions.*
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.exhibitions.main.ExhibitionsPresenter.ExhibitionsView as ExhView


class ExhibitionsFragment : BaseFragment<ExhView, ExhibitionsPresenter>(), ExhView {

    private val trigger: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    private val exhibitionsAdapter = ExhibitionsAdapter()

    override fun onResume() {
        super.onResume()
        trigger.onNext(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_exibitions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = super.onViewCreated(view, savedInstanceState).also { initUI() }

    override fun createPresenter() = ExhibitionsPresenter(this)

    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

    override fun loadMoreEvent() = exhibitionsAdapter.loadMoreEvent()


    override fun render(state: ExhibitionsViewState) {
        when (state.state) {
            INIT_DATA -> renderLoadingState()
            DATA -> renderDataState(state)
            ERROR -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
        progressView.show()
        exhibitionsView.hide()
    }


    private fun renderDataState(state: ExhibitionsViewState) {
        progressView.hide()
        exhibitionsView.show()
        exhibitionsAdapter.updateData(state.exhibitionItems)
    }

    private fun renderErrorState(state: ExhibitionsViewState) {
        Log.d("DEBUG", "error")
        state.error?.printStackTrace()
//        loadingIndicator.visible = false
//        helloWorldTextview.visible = false
        //Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun onEventReceived(action: ViewItemAction) {

        when (action.action) {
            ViewAction.SHARE -> generateShareIntent(action.item.exhibitionUrl!!)
                    .also { startActivity(Intent.createChooser(it, "Share images to..")) }

            ViewAction.WEB -> generateViewIntent(action.item.exhibitionUrl!!)
                    .also { startActivity(it) }


            ViewAction.DETAILS -> startActivityByClass(ExhibitionDetailsActivity::class.java)
            else -> throw Exception("Unhandled view action state")
        }

    }

    private fun initUI() {
        exhibitionsView.let {
            it.layoutManager = LinearLayoutManager(this.context)
            it.adapter = exhibitionsAdapter
            it.addItemDecoration(DividerItemDecoration(this.context, RecyclerView.HORIZONTAL))
        }

        exhibitionsAdapter.viewEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleLatest(200, TimeUnit.MILLISECONDS)
                .subscribe { onEventReceived(it) }
    }


}

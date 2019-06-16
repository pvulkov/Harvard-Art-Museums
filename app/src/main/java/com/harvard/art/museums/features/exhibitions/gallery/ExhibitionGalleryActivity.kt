package com.harvard.art.museums.features.exhibitions.gallery

import android.os.Bundle
import android.widget.Toast
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import kotlinx.android.synthetic.main.activity_exhibitions_details.*
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter.ExhibitionsDetailsView as ExdView
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryViewState as ExdViewState
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryPresenter as ExdPresenter
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryViewState.State.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class ExhibitionGalleryActivity : BaseActivity<ExdView, ExdPresenter>(), ExdView {

    private val trigger: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    private val adapter = ExhibitionsGalleryAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibitions_details)
        val id = intent.getIntExtra("exhibitionId", -1)
        initUI()
    }


    override fun onResume() {
        super.onResume()
        trigger.onNext(true)
    }

    override fun createPresenter() = ExdPresenter(this)

    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

    override fun loadMoreEvent(): Observable<ViewItemAction> {
        return adapter.loadMoreEvent()
    }

    override fun render(state: ExdViewState) {
        when (state.state) {
            INIT -> Unit
            LOAD_MORE -> Unit
            DATA -> renderDataState(state)
            ERROR -> Unit
        }
    }


    private fun renderDataState(state: ExdViewState) {
        adapter.updateData(state.exhibitionsList)

    }

    private fun renderErrorState(errorState: ExdViewState) {
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
//        setSupportActionBar(toolbar)

        pagerContainer.adapter = adapter

    }
}

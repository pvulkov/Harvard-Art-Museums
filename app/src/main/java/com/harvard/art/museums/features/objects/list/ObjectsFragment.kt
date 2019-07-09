package com.harvard.art.museums.features.objects.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseFragment
import com.harvard.art.museums.ext.generateActivityIntent
import com.harvard.art.museums.ext.hide
import com.harvard.art.museums.ext.show
import com.harvard.art.museums.ext.showToast
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryActivity
import com.harvard.art.museums.features.objects.details.ObjectDetailsActivity
import com.harvard.art.museums.features.objects.list.ObjectsPresenter.ObjectsView
import com.harvard.art.museums.features.objects.list.ObjectsViewState.State.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_objects.*


class ObjectsFragment : BaseFragment<ObjectsView, ObjectsPresenter>(), ObjectsView {

    private val objectsAdapter = ObjectsAdapter()
    private lateinit var objectsLayoutManager: ObjectGridlayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_objects, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun createPresenter() = ObjectsPresenter(this)

    override fun initDataEvent() = Observable.just(true).subscribeOn(Schedulers.io())

    override fun loadMoreEvent(): Observable<ObjectViewItem> = objectsAdapter.loadMoreEvent()


    override fun render(state: ObjectsViewState) {
        when (state.state) {
            LOADING -> renderLoadingState()
            DATA -> renderDataState(state)
            ERROR -> renderErrorState(state)
            INIT_DATA -> Unit
        }
    }

    private fun renderLoadingState() {
        progressView.show()
        objectsView.hide()
        actionRetry.hide()
    }

    private fun renderDataState(state: ObjectsViewState) {
        progressView.hide()
        objectsView.show()

        state.viewItems.apply {
            objectsLayoutManager.updateData(this)
            objectsAdapter.updateData(this)
        }

    }

    private fun renderErrorState(state: ObjectsViewState) {
        actionRetry.show()
        progressView.hide()
        showToast(state.error?.message)
    }


    private fun initUI() {
        objectsLayoutManager = ObjectGridlayoutManager(context)
        objectsView.apply {
            this.layoutManager = objectsLayoutManager
            this.adapter = objectsAdapter
            addItemDecoration(GridSpacingItemDecoration(8, 1, false))
        }


        objectsAdapter.itemClickedEvent()
                .subscribe({ startObjectDetailsActivity(it) }, {}, {})
                .also { disposable.add(it) }
    }


    private fun startObjectDetailsActivity(item: ObjectViewItem) {

        val extras = Bundle()
        extras.putInt("objectNumber", item.objectId)
        generateActivityIntent(ObjectDetailsActivity::class.java, extras)
                .also { startActivity(it) }
    }
}

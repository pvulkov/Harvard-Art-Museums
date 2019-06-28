package com.harvard.art.museums.features.objects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseFragment
import com.harvard.art.museums.ext.hide
import com.harvard.art.museums.ext.show
import com.harvard.art.museums.features.objects.ObjectsPresenter.ObjectsView
import com.harvard.art.museums.features.objects.ObjectsViewState.State.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_objects.*


class ObjectsFragment : BaseFragment<ObjectsView, ObjectsPresenter>(), ObjectsView {

    private val trigger: PublishSubject<Boolean> = PublishSubject.create()
    private val objectsAdapter = ObjectsAdapter()
    private lateinit var objectsLayoutManager: ObjectGridlayoutManager

    override fun onResume() {
        super.onResume()
        trigger.onNext(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_objects, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun createPresenter() = ObjectsPresenter(this)

    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())

    override fun loadMoreEvent(): Observable<ObjectViewItem> = objectsAdapter.viewEvents()


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
        Log.d("DEBUG", "error")
//        loadingIndicator.visible = false
//        Toast.makeText(this, "error ${state.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
        objectsLayoutManager = ObjectGridlayoutManager(context)
        objectsView.apply {
            this.layoutManager = objectsLayoutManager
            this.adapter = objectsAdapter
            addItemDecoration(GridSpacingItemDecoration(6, 10, false))
        }
    }
}

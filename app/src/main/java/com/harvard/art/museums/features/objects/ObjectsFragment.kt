package com.harvard.art.museums.features.objects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseFragment
import com.harvard.art.museums.features.exhibitions.ExhibitionsViewState.State.*
import com.harvard.art.museums.features.objects.ObjectsPresenter.ObjectsView
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class ObjectsFragment : BaseFragment<ObjectsView, ObjectsPresenter>(), ObjectsView {

    private val trigger: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
//    private val exhibitionsAdapter = ExhibitionsAdapter()

    override fun onResume() {
        super.onResume()
        trigger.onNext(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_objects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = super.onViewCreated(view, savedInstanceState).also { initUI() }

    override fun createPresenter() = ObjectsPresenter(this)

    override fun initDataEvent() = trigger.subscribeOn(Schedulers.io())


    override fun render(state: ObjectsViewState) {
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

    private fun renderDataState(state: ObjectsViewState) {
//        loadingIndicator.visible = false
//        exhibitionsAdapter.updateData(state.exhibitionItems)
    }

    private fun renderErrorState(state: ObjectsViewState) {
        Log.d("DEBUG", "error")
//        loadingIndicator.visible = false
//        helloWorldTextview.visible = false
        //Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }


    private fun initUI() {
//        exhibitionsView.let {
//            it.layoutManager = LinearLayoutManager(this.context)
//            it.adapter = exhibitionsAdapter
//            it.addItemDecoration(DividerItemDecoration(this.context, RecyclerView.HORIZONTAL))
//        }
    }
}

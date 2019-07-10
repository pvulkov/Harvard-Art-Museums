package com.harvard.art.museums.features.objects.details

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.exhibitions.gallery.ImageGalleryAdapter
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter.ObjectDetailsView
import com.harvard.art.museums.features.objects.details.ObjectDetailsViewState.State.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.object_details_layout.*
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter as Presenter
import com.harvard.art.museums.features.objects.details.ObjectDetailsViewState as ViewState


class ObjectDetailsActivity : BaseActivity<ObjectDetailsView, Presenter>(), ObjectDetailsView {

    private val imagesAdapter = ImageGalleryAdapter()
    private val objectsAdapter = ObjectDetailsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.object_details_layout)
        initUI()
    }

    override fun createPresenter() = Presenter(this)

    override fun render(state: ViewState) {

        when (state.state) {
            //TODO (pvalkov) review state
            NONE -> {
            }
            LOAD -> renderLoadingState()
            DATA -> renderDataState(state)
            ERROR -> renderErrorState(state)
        }
    }

    override fun loadData(): Observable<Int> = Observable.just(intent.getIntExtra("objectNumber", -1))


    private fun renderLoadingState() {
        progressView.show()
        mainContainer.hide()
    }


    private fun renderErrorState(state: ViewState) {
        progressView.hide()
        state.error?.printStackTrace()
//        //TODO (pvalkov) display "retry" button
//        Log.d("DEBUG", "---")
    }

    private fun renderDataState(state: ViewState) {

        progressView.hide()
        mainContainer.show()

        state.objectData!!.images.isNullOrEmpty()
                .ifTrue { galleryMainView.hide() }
                .ifFalse {

                    (state.objectData.images.size == 1)
                            .ifTrue { imageGalleryView.hide() }
                            .ifFalse { imagesAdapter.updateData(state.objectData.images) }

                    loadPoster(state.objectData.posterUrl!!, state.objectData.posterCaption)
                }


        objectDetailsDataView.adapter = objectsAdapter
        objectsAdapter.updateData(state.objectData!!.objectViewItems)

//        state.galleryObjectData?.apply {
//
//            exhDetailsDescription.text = this.description
//            exhDetailsDescription.movementMethod = LinkMovementMethod.getInstance()
//
//                exhGalleryView.adapter = imagesAdapter
//                exhGalleryView.layoutManager = LinearLayoutManager(this@ObjectDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
//                imagesAdapter.updateData(this.images)
//                imagesAdapter.viewEvents()
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .throttleLatest(200, TimeUnit.MILLISECONDS)
//                        .subscribe { loadPoster(it.baseimageurl, it.caption) }
//                        .also { disposable.add(it) }
    }

    private fun loadPoster(ulr: String, caption: String? = null) {

        obPosterCaption.applyTextAndVisibility(caption)

        Glide.with(this).load(ulr)
                .centerCrop()
                .placeholder(R.drawable.progress_anim_tint)
                .into(objectPoster)
        //.waitForLayout()
    }


    private fun initUI() {

        objectDetailsDataView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = objectsAdapter
            it.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
        }


        imageGalleryView.let {
            it.adapter = imagesAdapter
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }


    }


}
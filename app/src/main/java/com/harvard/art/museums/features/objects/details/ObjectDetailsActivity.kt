package com.harvard.art.museums.features.objects.details

import android.os.Bundle
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseActivity
import com.harvard.art.museums.ext.hide
import com.harvard.art.museums.ext.show
import com.harvard.art.museums.features.exhibitions.gallery.ImageGalleryAdapter
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter.ObjectDetailsView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.object_details_layout.*
import com.harvard.art.museums.features.objects.details.ObjectDetailsPresenter as Presenter
import com.harvard.art.museums.features.objects.details.ObjectDetailsViewState as ViewState


class ObjectDetailsActivity : BaseActivity<ObjectDetailsView, Presenter>(), ObjectDetailsView {

    private val imagesAdapter = ImageGalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.object_details_layout)
    }

    override fun createPresenter() = Presenter(this)

    override fun render(state: ViewState) {

        when (state.state) {
            //TODO (pvalkov) review state
//            NONE -> {
//            }
//            LOAD -> renderLoadingState()
//            DATA -> renderDataState(state)
//            ERROR -> renderErrorState(state)
        }
    }

    override fun loadData(): Observable<Int> = Observable.just(intent.getIntExtra("exhibitionId", -1))


    private fun renderLoadingState() {
        progressView.show()
        mainContent.hide()
    }


    private fun renderErrorState(state: ViewState) {
        progressView.hide()
        state.error?.printStackTrace()
//        //TODO (pvalkov) display "retry" button
//        Log.d("DEBUG", "---")
    }

    private fun renderDataState(state: ViewState) {

        progressView.hide()
        mainContent.show()

//        state.galleryObjectData?.apply {
//
//            exhDetailsDescription.text = this.description
//            exhDetailsDescription.movementMethod = LinkMovementMethod.getInstance()
//
//            exhDetailsFromTo.text = this.dateFromTo
//            exhDetailsLocation.text = this.location
//            exhDetailsTitle.text = this.title
//            this.poster?.let { p -> loadPoster(p.imageurl!!, p.caption) }
//
//
//            if (this.images.isEmpty() || this.images.size == 1) {
//                exhGalleryView.hide()
//            } else {
//                exhGalleryView.adapter = imagesAdapter
//                exhGalleryView.layoutManager = LinearLayoutManager(this@ObjectDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
//                imagesAdapter.updateData(this.images)
//                imagesAdapter.viewEvents()
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .throttleLatest(200, TimeUnit.MILLISECONDS)
//                        .subscribe { loadPoster(it.baseimageurl, it.caption) }
//                        .also { disposable.add(it) }
//            }
//
//        }
    }

    private fun loadPoster(ulr: String, caption: String) {
        exhPosterCaption.text = caption
        Glide.with(this).load(ulr)
                .centerCrop()
                .placeholder(R.drawable.progress_anim_tint)
                .into(exhDetailsPoster)
        //.waitForLayout()
    }


}
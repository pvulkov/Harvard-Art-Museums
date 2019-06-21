package com.harvard.art.museums.features.exhibitions.gallery.details

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseFragment
import com.harvard.art.museums.ext.generateArguments
import com.harvard.art.museums.ext.hide
import com.harvard.art.museums.ext.show
import com.harvard.art.museums.features.exhibitions.gallery.ExhibitionGalleryAdapter
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsViewState.State.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_exibition.*
import java.util.concurrent.TimeUnit
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsPresenter as GalleryPresenter
import com.harvard.art.museums.features.exhibitions.gallery.details.GalleryDetailsPresenter.GalleryDetailsView as GalleryView


class GalleryDetailsFragment : BaseFragment<GalleryView, GalleryPresenter>(), GalleryView {

    private val galleryAdapter = ExhibitionGalleryAdapter()


    companion object {

        private const val KEY = "Exhibition.id"

        fun newInstance(exhibitionId: Int): GalleryDetailsFragment {
            return GalleryDetailsFragment().also { it.arguments = generateArguments(KEY, exhibitionId) }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_exibition, container, false)
    }

    override fun createPresenter() = GalleryPresenter(this)

    override fun render(state: GalleryDetailsViewState) {

        when (state.state) {
            NONE -> {
            }
            LOAD -> renderLoadingState()
            DATA -> renderDataState(state)
            ERROR -> renderErrorState(state)


        }
    }

    override fun loadData(): Observable<Int> = Observable.just(arguments?.getInt(KEY))


    private fun renderLoadingState() {
        progressView.show()
        mainContent.hide()
    }


    private fun renderErrorState(state: GalleryDetailsViewState) {
        progressView.hide()
        state.error?.printStackTrace()
        //TODO (pvalkov) display "retry" button
        Log.d("DEBUG", "---")
    }

    private fun renderDataState(state: GalleryDetailsViewState) {

        progressView.hide()
        mainContent.show()

        state.galleryObjectData?.apply {

            exhDetailsDescription.text = this.description
            exhDetailsDescription.setMovementMethod(LinkMovementMethod.getInstance())

            exhDetailsFromTo.text = this.dateFromTo
            exhDetailsLocation.text = this.location
            exhDetailsTitle.text = this.title
            this.poster?.let { p -> loadPoster(p.imageurl, p.caption) }


            if (this.images.isEmpty()) {
                exhGalleryView.hide()
            } else {
                exhGalleryView.adapter = galleryAdapter
                exhGalleryView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                galleryAdapter.updateData(this.images)
                galleryAdapter.viewEvents()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .throttleLatest(200, TimeUnit.MILLISECONDS)
                        .subscribe { loadPoster(it.baseimageurl, it.caption) }
                        .also { disposable.add(it) }
            }

        }
    }

    private fun loadPoster(ulr: String, caption: String) {
        exhPosterCaption.text = caption
        Glide.with(context).load(ulr)
                .centerCrop()
                .placeholder(R.drawable.progress_anim_tint)
                .into(exhDetailsPoster)
        //.waitForLayout()
    }


}
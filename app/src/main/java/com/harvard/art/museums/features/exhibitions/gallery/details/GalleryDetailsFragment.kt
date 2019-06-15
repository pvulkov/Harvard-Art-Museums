package com.harvard.art.museums.features.exhibitions.gallery.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.base.BaseFragment
import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.generateArguments
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

    override fun render(viewState: GalleryDetailsViewState) {

        when (viewState.state) {
            NONE -> {
            }
            LOAD -> {
            }
            DATA -> renderDataState(viewState)
            ERROR -> renderErrorState(viewState)


        }
    }

    override fun loadData(): Observable<Int> = Observable.just(arguments?.getInt(KEY))


    override fun onResume() {
        super.onResume()

        exhGalleryView.apply {
            adapter = galleryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }
    }


    private fun renderErrorState(viewState: GalleryDetailsViewState) {

        Log.d("DEBUG", "---")
    }

    private fun renderDataState(viewState: GalleryDetailsViewState) {


        exhDetailsDescription.text = viewState.galleryObjectData?.description


        //TODO (pvalkov)
        galleryAdapter.updateData(viewState.galleryObjectData!!.images)
        galleryAdapter.viewEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleLatest(200, TimeUnit.MILLISECONDS)
                .subscribe { loadPoster(it) }
                .also { disposable.add(it) }
    }

    private fun loadPoster(image: Image) {
        Glide.with(context).load(image.baseimageurl)
                .centerCrop()
                .placeholder(R.drawable.image_progress_animation)
                .into(exhDetailsPoster)
                //.waitForLayout()
    }


}
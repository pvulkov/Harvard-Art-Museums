package com.harvard.art.museums.features.exhibitions.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.ext.thumbUrl
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.gallery_view_item.view.*

class ImageGalleryAdapter : RecyclerView.Adapter<ImageGalleryAdapter.GalleryItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val galleryList = mutableListOf<Image>()
    private lateinit var viewEvent: PublishSubject<Image>


    fun updateData(galleryList: List<Image>) {
        this.galleryList.setData(galleryList)
        notifyDataSetChanged()
    }

    fun viewEvents() = viewEvent

    override fun getItemCount() = galleryList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GalleryItemViewHolder(inflater.inflate(R.layout.gallery_view_item, parent, false))
    }


    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        holder.setData(galleryList[position], viewEvent, disposable)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        viewEvent = PublishSubject.create<Image>()
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        viewEvent.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


    class GalleryItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


        fun setData(image: Image, subject: PublishSubject<Image>, disposable: CompositeDisposable) {


            itemView.galleryImage.clicks()
                    .subscribe { subject.onNext(image) }
                    .also { disposable.add(it) }

            Glide.with(view).load(image.baseimageurl.thumbUrl()).centerCrop().into(itemView.galleryImage).waitForLayout()

        }
    }


}
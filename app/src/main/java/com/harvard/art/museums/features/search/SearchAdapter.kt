package com.harvard.art.museums.features.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.setData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_view_item.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val itemsList = mutableListOf<String>()
    private lateinit var viewEvent: PublishSubject<Image>


    fun updateData(galleryList: List<String>) {
        this.itemsList.setData(galleryList)
        notifyDataSetChanged()
    }

    fun viewEvents() = viewEvent

    override fun getItemCount() = itemsList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_view_item, parent, false))
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setData(itemsList[position], viewEvent, disposable)
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


    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


        fun setData(text: String, subject: PublishSubject<Image>, disposable: CompositeDisposable) {

            itemView.itemText.text = text

//            itemView.galleryImage.clicks()
//                    .subscribe { subject.onNext(image) }
//                    .also { disposable.add(it) }
//
//            Glide.with(view).load(image.baseimageurl.thumbUrl()).centerCrop().into(itemView.galleryImage).waitForLayout()

        }
    }


}
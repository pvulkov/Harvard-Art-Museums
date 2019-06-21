package com.harvard.art.museums.features.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.data.pojo.Image
import com.harvard.art.museums.ext.isValidUrl
import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.ext.thumbUrl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.result_exhibition_view_item.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val items = mutableListOf<SearchResultViewItem>()
    private lateinit var viewEvent: PublishSubject<Image>


    fun updateData(items: List<SearchResultViewItem>) {
        this.items.setData(items)
        notifyDataSetChanged()
    }

    fun viewEvents() = viewEvent

    override fun getItemCount() = items.size

    fun getItemSpan(position: Int): Int = items[position].span


    override fun getItemViewType(position: Int) = items[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (SearchResultViewType.values()[viewType]) {
            SearchResultViewType.OBJECT -> ItemViewHolder(inflater.inflate(R.layout.result_object_view_item, parent, false))
            SearchResultViewType.EXHIBITION -> ItemViewHolder(inflater.inflate(R.layout.result_exhibition_view_item, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setData(items[position], viewEvent, disposable)
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


        fun setData(item: SearchResultViewItem, subject: PublishSubject<Image>, disposable: CompositeDisposable) {

            itemView.resultText.text = item.title

//            itemView.galleryImage.clicks()
//                    .subscribe { subject.onNext(image) }
//                    .also { disposable.add(it) }

            if (item.imageUrl.isValidUrl())
                Glide.with(view).load(item.imageUrl!!.thumbUrl()).centerCrop().into(itemView.resultImage).waitForLayout()

        }
    }


}
package com.harvard.art.museums.features.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.isValidUrl
import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.ext.thumbUrl
import com.harvard.art.museums.features.search.SearchResultViewType.*
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.result_exhibition_view_item.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val items = mutableListOf<SearchResultViewItem>()
    private val viewEventsSubject = PublishSubject.create<SearchResultViewItem>()


    fun updateData(items: List<SearchResultViewItem>) {
        this.items.setData(items)
        notifyDataSetChanged()
    }

    fun viewEvents() = viewEventsSubject

    override fun getItemCount() = items.size

    fun getItemSpan(position: Int): Int = items[position].span


    override fun getItemViewType(position: Int) = items[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (values()[viewType]) {
            OBJECT -> ItemViewHolder(inflater.inflate(R.layout.result_object_view_item, parent, false))
            EXHIBITION -> ItemViewHolder(inflater.inflate(R.layout.result_exhibition_view_item, parent, false))
            RECENT -> ItemViewHolder(inflater.inflate(R.layout.result_recent_search_view_item, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        bindActionItem(holder.view, item)
        holder.setData(item)
    }

    private fun bindActionItem(view: View, item: SearchResultViewItem) {
        RxView.clicks(view)
                .map { item }
                .subscribe(viewEventsSubject)
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        viewEventsSubject.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


        fun setData(item: SearchResultViewItem) {

            itemView.resultText.text = item.text

//            itemView.galleryImage.clicks()
//                    .subscribe { subject.onNext(image) }
//                    .also { disposable.add(it) }

            if (item.imageUrl.isValidUrl())
                Glide.with(view).load(item.imageUrl!!.thumbUrl()).centerCrop().into(itemView.resultImage).waitForLayout()

        }
    }


}
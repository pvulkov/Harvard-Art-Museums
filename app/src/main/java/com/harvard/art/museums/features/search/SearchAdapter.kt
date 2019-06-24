package com.harvard.art.museums.features.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.*
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

    fun viewEvents() = viewEventsSubject.share()

    override fun getItemCount() = items.size

    fun getItemSpan(position: Int): Int = items[position].span


    override fun getItemViewType(position: Int) = items[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (values()[viewType]) {
            OBJECT -> ItemViewHolder(inflater.inflate(R.layout.result_object_view_item, parent, false))
            EXHIBITION -> ItemViewHolder(inflater.inflate(R.layout.result_exhibition_view_item, parent, false))
            RECENT_SEARCH -> ItemViewHolder(inflater.inflate(R.layout.result_recent_search_view_item, parent, false))
            RECENT_EXHIBITION -> ItemViewHolder(inflater.inflate(R.layout.result_recent_search_view_item, parent, false))
            RECENT_OBJECT -> ItemViewHolder(inflater.inflate(R.layout.result_recent_search_view_item, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        bindActionItem(holder.view, position)
        holder.setData(items[position])
    }

    private fun bindActionItem(view: View, position: Int) {


        val item = items[position]

        Log.d("DEBUG", "binding " + item.viewType)

        val viewType = when (item.viewType) {
            OBJECT -> RECENT_OBJECT
            EXHIBITION -> RECENT_EXHIBITION
            else -> item.viewType
        }


        RxView.clicks(view)
                .map { item.also { it.viewType = viewType } }
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


            item.imageUrl.isValidUrl()
                    .ifTrue {
                        Glide.with(view).load(item.imageUrl!!.thumbUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(itemView.resultImage).waitForLayout()
                    }.ifFalse {
                        Glide.with(view).load(R.drawable.ic_search_tinted)
                                .apply(RequestOptions.circleCropTransform())
                                .into(itemView.resultImage).waitForLayout()
                    }


        }
    }


}
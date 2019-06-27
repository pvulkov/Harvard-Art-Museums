package com.harvard.art.museums.features.objects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.objects.ViewItemType.DATA
import com.harvard.art.museums.features.objects.ViewItemType.values
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.result_exhibition_view_item.view.*

class ObjectsAdapter : RecyclerView.Adapter<ObjectsAdapter.ItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val items = mutableListOf<ObjectViewItem>()
    private val viewEventsSubject = PublishSubject.create<ObjectViewItem>()


    fun updateData(items: List<ObjectViewItem>) {
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
            DATA -> ItemViewHolder(inflater.inflate(R.layout.result_object_view_item, parent, false))
            else -> throw  Exception("Unhandled else case")
        }
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //  bindActionItem(holder.view, position)
        holder.setData(items[position])
    }

//    private fun bindActionItem(view: View, position: Int) {
//
//
//        val item = items[position]
//
//        Log.d("DEBUG", "binding " + item.viewType)
//
//        val viewType = when (item.viewType) {
//            OBJECT -> RECENT_OBJECT
//            EXHIBITION -> RECENT_EXHIBITION
//            else -> item.viewType
//        }
//
//
//        RxView.clicks(view)
//                .map { item.also { it.viewType = viewType } }
//                .subscribe(viewEventsSubject)
//    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        viewEventsSubject.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


        fun setData(item: ObjectViewItem) {

            itemView.resultText.text = item.text

            item.image.hasValidUrl()
                    .ifTrue {
                        Glide.with(view).load(item.image!!.baseimageurl.thumbUrl())
                                .fitCenter()
                                .into(itemView.resultImage).waitForLayout()
                    }.ifFalse {
                        Glide.with(view).load(R.drawable.ic_search_tinted)
                                .fitCenter()
                                .into(itemView.resultImage).waitForLayout()
                    }


        }
    }


}
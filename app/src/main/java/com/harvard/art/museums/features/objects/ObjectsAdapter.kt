package com.harvard.art.museums.features.objects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.*
import com.harvard.art.museums.features.objects.ObjectsAdapter.ItemViewHolder.LoaderViewHolder
import com.harvard.art.museums.features.objects.ObjectsAdapter.ItemViewHolder.ObjectViewHolder
import com.harvard.art.museums.features.exhibitions.data.ViewItemType.ViewType.*
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.object_view_item.view.*

class ObjectsAdapter : RecyclerView.Adapter<ObjectsAdapter.ItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val items = mutableListOf<ObjectViewItem>()
    private val viewObjectSubject = PublishSubject.create<ObjectViewItem>()


    fun updateData(items: List<ObjectViewItem>) {
        this.items.setData(items)
        notifyDataSetChanged()
    }

    fun viewEvents() = viewObjectSubject.share()

    override fun getItemCount() = items.size


    override fun getItemViewType(position: Int) = items[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (values()[viewType]) {
            DATA -> ObjectViewHolder(inflater.inflate(R.layout.object_view_item, parent, false))
            LOADER -> LoaderViewHolder(inflater.inflate(R.layout.exhibitions_loader_view_item, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        bindActionItem(holder.view, position)
        holder.setData(items[position])
    }

    private fun bindActionItem(view: View, position: Int) {
        val item = items[position]
        (item.viewType == DATA)
                .ifTrue { RxView.clicks(view).map { item }.subscribe(viewObjectSubject) }
                .ifFalse { viewObjectSubject.onNext(item) }
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        viewObjectSubject.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }

    sealed class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        abstract fun setData(item: ObjectViewItem)


        class ObjectViewHolder(view: View) : ItemViewHolder(view) {


            override fun setData(item: ObjectViewItem) {

                itemView.objectText.text = item.text
                item.image.hasValidUrl()
                        .ifTrue {
                            Glide.with(view).load(item.image!!.baseimageurl.thumbUrl())
                                    .into(itemView.objectImage).waitForLayout()
                        }.ifFalse {
                            Glide.with(view).load(R.drawable.ic_search_tinted)
                                    .into(itemView.objectImage).waitForLayout()
                        }
            }
        }

        class LoaderViewHolder(view: View) : ItemViewHolder(view) {
            override fun setData(item: ObjectViewItem) {
//                publisher.onNext(item.toViewItemAction(ViewAction.LOAD_MORE))
            }
        }
    }

}
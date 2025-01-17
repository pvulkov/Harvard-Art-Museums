package com.harvard.art.museums.features.exhibitions.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.isValidUrl
import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.ext.show
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewAction
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import com.harvard.art.museums.features.exhibitions.data.toViewItemAction
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.exhibitions_data_view_item.view.*
import com.harvard.art.museums.features.exhibitions.data.ViewItemType.ViewType.*


class ExhibitionsAdapter : RecyclerView.Adapter<ExhibitionsAdapter.ExhibitionViewHolder>() {


    private val disposable = CompositeDisposable()
    private val exhibitionItemsList = mutableListOf<ExhibitionViewItem>()
    private lateinit var viewEvent: PublishSubject<ViewItemAction>


    fun updateData(newExhibitionItems: List<ExhibitionViewItem>) {
        exhibitionItemsList.setData(newExhibitionItems)
        notifyDataSetChanged()
    }


    fun loadMoreEvent() = viewEvent.filter { it.action == ViewAction.LOAD_MORE }


    fun viewEvents(): Observable<ViewItemAction> {
        return viewEvent.filter { it.action != ViewAction.LOAD_MORE }
    }

    override fun getItemCount() = exhibitionItemsList.size

    override fun getItemViewType(position: Int): Int = exhibitionItemsList[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (values()[viewType]) {
            DATA -> ExhibitionViewHolder.ExhibitionItemViewHolder(inflater.inflate(R.layout.exhibitions_data_view_item, parent, false))
            LOADER -> ExhibitionViewHolder.LoaderItemViewHolder(inflater.inflate(R.layout.exhibitions_loader_view_item, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ExhibitionViewHolder, position: Int) {
        holder.setData(exhibitionItemsList[position], viewEvent)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        viewEvent = PublishSubject.create<ViewItemAction>()
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        viewEvent.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


    sealed class ExhibitionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        abstract fun setData(item: ExhibitionViewItem, publisher: PublishSubject<ViewItemAction>, disposable: CompositeDisposable? = null)

        class ExhibitionItemViewHolder(view: View) : ExhibitionViewHolder(view) {


            override fun setData(item: ExhibitionViewItem, publisher: PublishSubject<ViewItemAction>, disposable: CompositeDisposable?) {
                itemView.exhibitionTitle.text = item.title
                itemView.exhibitionFromTo.text = item.exhibitionFromTo

                item.shortDescription?.let {
                    itemView.exhibitionDescription.show()
                    itemView.exhibitionDescription.text = it
                }

                item.exhibitionLocation?.let {
                    itemView.exhibitionLocation.show()
                    itemView.exhibitionLocation.text = it
                }

                val list = mutableListOf<Observable<ViewAction>>()
                list.add(itemView.actionDetails.clicks().map { ViewAction.DETAILS })


                if (item.exhibitionUrl.isValidUrl()) {
                    itemView.actionShare.show()
                    itemView.actionOpenWeb.show()
                    list.add(itemView.actionShare.clicks().map { ViewAction.SHARE })
                    list.add(itemView.actionOpenWeb.clicks().map { ViewAction.WEB })
                }


                Observable.merge(list)
                        .flatMap { Observable.just(item.toViewItemAction(it)) }
                        .subscribe { publisher.onNext(it) }
                        .also { disposable?.add(it) }


                Glide.with(view).load(item.imageUrl).centerCrop().into(itemView.exhibitionImage).waitForLayout()
                Glide.with(view).load(item.openStatus).centerCrop().into(itemView.exhibitionOpenStatus).waitForLayout()

            }
        }


        class LoaderItemViewHolder(view: View) : ExhibitionViewHolder(view) {
            override fun setData(item: ExhibitionViewItem, publisher: PublishSubject<ViewItemAction>, disposable: CompositeDisposable?) {
                publisher.onNext(item.toViewItemAction(ViewAction.LOAD_MORE))
            }
        }
    }
}
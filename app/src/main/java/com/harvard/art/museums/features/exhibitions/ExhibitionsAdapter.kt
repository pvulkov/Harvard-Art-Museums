package com.harvard.art.museums.features.exhibitions

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.setData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.exhibitions_data_view_item.view.*
import com.harvard.art.museums.features.exhibitions.ViewItemType.*

class ExhibitionsAdapter : RecyclerView.Adapter<ExhibitionsAdapter.ExhibitionViewHolder>() {


    private val disposable = CompositeDisposable()
    private val exhibitionItemsList = mutableListOf<ExhibitionViewItem>()
    lateinit var publisher: PublishSubject<ExhibitionViewItem>


    fun updateData(newExhibitionItems: List<ExhibitionViewItem>) {
        exhibitionItemsList.setData(newExhibitionItems)
        notifyDataSetChanged()
    }


    fun loadMoreEvent(): Observable<ExhibitionViewItem> {
        return publisher
    }

    override fun getItemCount() = exhibitionItemsList.size

    override fun getItemViewType(position: Int): Int = exhibitionItemsList[position].type.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (values()[viewType]) {
            DATA -> ExhibitionViewHolder.ExhibitionItemViewHolder(inflater.inflate(R.layout.exhibitions_data_view_item, parent, false))
            LOADER -> ExhibitionViewHolder.LoaderItemViewHolder(inflater.inflate(R.layout.exhibitions_loader_view_item, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ExhibitionViewHolder, position: Int) {

        holder.setData(exhibitionItemsList[position], publisher)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        publisher = PublishSubject.create<ExhibitionViewItem>()
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        publisher.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


//    class ExhibitionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//
//        fun setData(item: ExhibitionViewItem) {
//            itemView.exhibitionTitle.text = item.title
//            itemView.exhibitionFromTo.text = item.exhibitionFromTo
//            itemView.exhibitionLocation.text = "TODO"
//
//            Glide.with(view).load(item.imageUrl).centerCrop().into(itemView.exhibitionImage).waitForLayout()
//
//        }
//    }
//
//
//    class LoaderItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//
//        fun setData(item: ExhibitionViewItem) {
////            itemView.exhibitionTitle.text = item.title
////            itemView.exhibitionFromTo.text = item.exhibitionFromTo
////            itemView.exhibitionLocation.text = "TODO"
////
////            Glide.with(view).load(item.imageUrl).centerCrop().into(itemView.exhibitionImage).waitForLayout()
//
//        }
//    }

    sealed class ExhibitionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        abstract fun setData(item: ExhibitionViewItem, publisher: PublishSubject<ExhibitionViewItem>)

        class ExhibitionItemViewHolder(view: View) : ExhibitionViewHolder(view) {

            override fun setData(item: ExhibitionViewItem, publisher: PublishSubject<ExhibitionViewItem>) {
                itemView.exhibitionTitle.text = item.title
                itemView.exhibitionFromTo.text = item.exhibitionFromTo
                itemView.exhibitionLocation.text = "TODO"

                Glide.with(view).load(item.imageUrl).centerCrop().into(itemView.exhibitionImage).waitForLayout()

            }
        }


        class LoaderItemViewHolder(view: View) : ExhibitionViewHolder(view) {

           override fun setData(item: ExhibitionViewItem,  publisher: PublishSubject<ExhibitionViewItem>) {
               Log.d("DEBUG", "LoaderItemViewHolder --->> ")
               publisher.onNext(item)
//            itemView.exhibitionTitle.text = item.title
//            itemView.exhibitionFromTo.text = item.exhibitionFromTo
//            itemView.exhibitionLocation.text = "TODO"
//
//            Glide.with(view).load(item.imageUrl).centerCrop().into(itemView.exhibitionImage).waitForLayout()

            }
        }
    }
}
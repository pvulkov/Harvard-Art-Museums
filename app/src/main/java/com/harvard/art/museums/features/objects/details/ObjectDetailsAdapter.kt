package com.harvard.art.museums.features.objects.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.setData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.exhibitions_data_view_item.view.*


class ObjectDetailsAdapter : RecyclerView.Adapter<ObjectDetailsAdapter.ExhibitionItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val exhibitionItemsList = mutableListOf<String>()
//    private lateinit var viewEvent: PublishSubject<ViewItemAction>


    fun updateData(newExhibitionItems: List<String>) {
        exhibitionItemsList.setData(newExhibitionItems)
        notifyDataSetChanged()
    }


//    fun loadMoreEvent() = viewEvent.filter { it.action == ViewAction.LOAD_MORE }
//
//
//    fun viewEvents(): Observable<ViewItemAction> {
//        return viewEvent.filter { it.action != ViewAction.LOAD_MORE }
//    }

    override fun getItemCount() = exhibitionItemsList.size

//    override fun getItemViewType(position: Int): Int = exhibitionItemsList[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExhibitionItemViewHolder(inflater.inflate(R.layout.exhibitions_data_view_item, parent, false))
    }


    override fun onBindViewHolder(holder: ExhibitionItemViewHolder, position: Int) {
        holder.setData(exhibitionItemsList[position])
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
//        viewEvent = PublishSubject.create<ViewItemAction>()
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
//        viewEvent.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


    class ExhibitionItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun setData(text: String) {
            itemView.exhibitionTitle.text = text
            itemView.exhibitionFromTo.text = text

//            item.shortDescription?.let {
//                itemView.exhibitionDescription.show()
//                itemView.exhibitionDescription.text = it
//            }
//
//            item.exhibitionLocation?.let {
//                itemView.exhibitionLocation.show()
//                itemView.exhibitionLocation.text = it
//            }
//
//            val list = mutableListOf<Observable<ViewAction>>()
//            list.add(itemView.actionDetails.clicks().map { ViewAction.DETAILS })
//
//
//            if (item.exhibitionUrl.isValidUrl()) {
//                itemView.actionShare.show()
//                itemView.actionOpenWeb.show()
//                list.add(itemView.actionShare.clicks().map { ViewAction.SHARE })
//                list.add(itemView.actionOpenWeb.clicks().map { ViewAction.WEB })
//            }

        }
    }

}
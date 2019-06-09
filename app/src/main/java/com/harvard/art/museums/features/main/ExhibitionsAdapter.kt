package com.harvard.art.museums.features.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.setData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.exhibitions_view_item.view.*

class ExhibitionsAdapter : RecyclerView.Adapter<ExhibitionsAdapter.ExhibitionItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val exhibitionItemsList = mutableListOf<ExhibitionViewItem>()
    lateinit var clickSubject: PublishSubject<ExhibitionViewItem>


    fun updateData(newExhibitionItems: List<ExhibitionViewItem>) {
        exhibitionItemsList.setData(newExhibitionItems)
        notifyDataSetChanged()
    }


    override fun getItemCount() = exhibitionItemsList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExhibitionItemViewHolder(inflater.inflate(R.layout.exhibitions_view_item, parent, false))

    }


    override fun onBindViewHolder(holder: ExhibitionItemViewHolder, position: Int) {
        holder.setData(exhibitionItemsList[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        clickSubject = PublishSubject.create<ExhibitionViewItem>()
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        clickSubject.onComplete()
        if (!disposable.isDisposed)
            disposable.dispose()
    }


    class ExhibitionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setData(item: ExhibitionViewItem) {
            itemView.exhibitionTitle.text = item.title
        }
    }
}
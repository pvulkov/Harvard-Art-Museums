package com.harvard.art.museums.features.objects.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.applyTextAndVisibility
import com.harvard.art.museums.ext.setData
import io.reactivex.disposables.CompositeDisposable
import  com.harvard.art.museums.features.objects.details.ObjectDetailsAdapter.ObjectItemViewHolder.*
import kotlinx.android.synthetic.main.ob_details_gallery_text_view_item.view.*
import kotlinx.android.synthetic.main.ob_details_identification_view_item.view.*
import kotlinx.android.synthetic.main.ob_details_physical_desc_view_item.view.*
import kotlinx.android.synthetic.main.ob_details_provenance_view_item.view.*


class ObjectDetailsAdapter : RecyclerView.Adapter<ObjectDetailsAdapter.ObjectItemViewHolder>() {


    private val disposable = CompositeDisposable()
    private val objectViewItems = mutableListOf<ObjectViewItem>()
//    private lateinit var viewEvent: PublishSubject<ViewItemAction>


    fun updateData(objectViewItems: List<ObjectViewItem>) {
        this.objectViewItems.setData(objectViewItems)
        notifyDataSetChanged()
    }


//
//    fun viewEvents(): Observable<ViewItemAction> {
//        return viewEvent.filter { it.action != ViewAction.LOAD_MORE }
//    }

    override fun getItemCount() = objectViewItems.size

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ObjectItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (objectViewItems[position]) {
            is GalleryTextViewItem -> GalleryTextViewHolder(inflater.inflate(R.layout.ob_details_gallery_text_view_item, parent, false))
            is IdentificationViewItem -> IdentificationViewHolder(inflater.inflate(R.layout.ob_details_identification_view_item, parent, false))
            is PhysicalDescriptionsViewItem -> PhysicalDescriptionsViewHolder(inflater.inflate(R.layout.ob_details_physical_desc_view_item, parent, false))
            is ProvenanceViewItem -> ProvenanceViewHolder(inflater.inflate(R.layout.ob_details_provenance_view_item, parent, false))


            else -> throw Exception("Unhandled view type holder")
        }
    }


    override fun onBindViewHolder(holder: ObjectItemViewHolder, position: Int) {
        holder.setData(objectViewItems[position])
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

    sealed class ObjectItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setData(item: ObjectViewItem)


        class GalleryTextViewHolder(view: View) : ObjectItemViewHolder(view) {
            override fun setData(item: ObjectViewItem) {
                (item as GalleryTextViewItem).apply {
                    itemView.galleryText.text = text
                }
            }
        }

        class IdentificationViewHolder(view: View) : ObjectItemViewHolder(view) {
            override fun setData(item: ObjectViewItem) {
                (item as IdentificationViewItem).apply {
                    itemView.objectNumber.applyTextAndVisibility(objectNumber)
                    itemView.objectTitle.applyTextAndVisibility(title)
                    itemView.objectOtherTitles.applyTextAndVisibility(otherTitles)
                    itemView.objectClassification.applyTextAndVisibility(classification)
                    itemView.objectWorkType.applyTextAndVisibility(worktypes)
                    itemView.objectDate.applyTextAndVisibility(dated)
                    itemView.objectPlaces.applyTextAndVisibility(places)
                    itemView.objectPeriod.applyTextAndVisibility(period)
                    itemView.objectCulture.applyTextAndVisibility(culture)
                }
            }
        }

        class PhysicalDescriptionsViewHolder(view: View) : ObjectItemViewHolder(view) {
            override fun setData(item: ObjectViewItem) {
                (item as PhysicalDescriptionsViewItem).apply {
                    itemView.objectMedium.applyTextAndVisibility(medium)
                    itemView.objectDimensions.applyTextAndVisibility(dimensions)
                }
            }
        }


        class ProvenanceViewHolder(view: View) : ObjectItemViewHolder(view) {
            override fun setData(item: ObjectViewItem) {
                (item as ProvenanceViewItem).apply {
                    itemView.provenanceText.applyTextAndVisibility(text)
                }
            }
        }

    }

}
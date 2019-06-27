package com.harvard.art.museums.features.objects

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.harvard.art.museums.ext.setData

class ObjectGridlayoutManager(ctx: Context) : GridLayoutManager(ctx, 6) {


    init {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                if (objectsList.isNotEmpty()) {

                    if (objectsList.size % 2 == 1 && objectsList.size - 1 == position)
                        return 6


                    val imageData = objectsList[position]
                    if (position == 0 || position % 2 == 0) {

                        val nextPhotoData = objectsList[position + 1]

                        if (isVertical(imageData) && isVertical(nextPhotoData)) {
                            return 3
                        }

                        if (isHorizontal(imageData) && isHorizontal(nextPhotoData)) {
                            return 3
                        }

                        if (isVertical(imageData) && isHorizontal(nextPhotoData)) {
                            return 2
                        }

                        if (isHorizontal(imageData) && isVertical(nextPhotoData)) {
                            return 4
                        }

//                        return AlbumAdapter.PhotoViewType.TYPE_FULL_HORIZONTAL.typeId

                    } else {

                        val prevImageData = objectsList[position - 1]

                        if (isVertical(imageData) && isVertical(prevImageData)) {
                            return 3
                        }

                        if (isHorizontal(imageData) && isHorizontal(prevImageData)) {
                            return 3
                        }

                        if (isVertical(imageData) && isHorizontal(prevImageData)) {
                            return 2
                        }

                        if (isHorizontal(imageData) && isVertical(prevImageData)) {
                            return 4
                        }

                    }

                }

                return 6
            }
        }
    }

    private var objectsList = mutableListOf<ObjectViewItem>()

    fun updateData(newSiteAlbumDataList: List<ObjectViewItem>) {
        this.objectsList.setData(newSiteAlbumDataList)
    }

    private fun isVertical(ovi: ObjectViewItem) = ovi.image?.let { it.height >= it.width } ?: false

    private fun isHorizontal(ovi: ObjectViewItem) = ovi.image?.let { it.width > it.height } ?: false

}
package com.harvard.art.museums.features.objects.list

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.harvard.art.museums.ext.setData

private const val MAX_SPAN = 8
private const val SPAN_SMALL = 3
private const val SPAN_MEDIUM = 4
private const val SPAN_LARGE = 5

class ObjectGridlayoutManager(ctx: Context) : GridLayoutManager(ctx, MAX_SPAN) {


    init {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                if (objectsList.isNotEmpty()) {


                    if (objectsList.size % 2 == 1 && objectsList.size - 1 == position)
                        return MAX_SPAN


                    val imageData = objectsList[position]

                    if (position == 0 || position % 2 == 0) {

                        val nextPhotoData = objectsList[position + 1]

                        //CASE #1
                        if (isVertical(imageData) && isVertical(nextPhotoData)) {
                            return SPAN_MEDIUM
                        }

                        //CASE #2
                        if (isHorizontal(imageData) && isHorizontal(nextPhotoData)) {
                            return SPAN_MEDIUM
                        }

                        //CASE #3
                        if (isDefaultImage(imageData) && isDefaultImage(nextPhotoData)) {
                            return SPAN_MEDIUM
                        }

                        //CASE #4
                        if (isVertical(imageData) && isHorizontal(nextPhotoData)) {
                            return SPAN_SMALL
                        }

                        //CASE #5
                        if (isVertical(imageData) && isDefaultImage(nextPhotoData)) {
                            return SPAN_MEDIUM
                        }

                        //CASE #6
                        if (isHorizontal(imageData) && isVertical(nextPhotoData)) {
                            return SPAN_LARGE
                        }


                        //CASE #7
                        if (isHorizontal(imageData) && isDefaultImage(nextPhotoData)) {
                            return SPAN_LARGE
                        }

                        //CASE #8
                        if (isDefaultImage(imageData) && isVertical(nextPhotoData)) {
                            return SPAN_MEDIUM
                        }

                        //CASE #9
                        if (isDefaultImage(imageData) && isHorizontal(nextPhotoData)) {
                            return SPAN_SMALL
                        }

//                        return AlbumAdapter.PhotoViewType.TYPE_FULL_HORIZONTAL.typeId

                    } else {

                        val prevImageData = objectsList[position - 1]

                        //CASE #1
                        if (isVertical(imageData) && isVertical(prevImageData)) {
                            return SPAN_MEDIUM
                        }

                        //CASE #2
                        if (isHorizontal(imageData) && isHorizontal(prevImageData)) {
                            return SPAN_MEDIUM
                        }


                        //CASE #3
                        if (isDefaultImage(imageData) && isDefaultImage(prevImageData)) {
                            return SPAN_MEDIUM
                        }


                        //CASE #4
                        if (isVertical(imageData) && isHorizontal(prevImageData)) {
                            return SPAN_SMALL
                        }


                        //CASE #5
                        if (isVertical(imageData) && isDefaultImage(prevImageData)) {
                            return SPAN_MEDIUM
                        }


                        //CASE #6
                        if (isHorizontal(imageData) && isVertical(prevImageData)) {
                            return SPAN_LARGE
                        }

                        //CASE #7
                        if (isHorizontal(imageData) && isDefaultImage(prevImageData)) {
                            return SPAN_LARGE
                        }

                        //CASE #8
                        if (isDefaultImage(imageData) && isVertical(prevImageData)) {
                            return SPAN_MEDIUM
                        }


                        //CASE #9
                        if (isDefaultImage(imageData) && isHorizontal(prevImageData)) {
                            return SPAN_SMALL
                        }
                    }

                }

                return MAX_SPAN
            }
        }
    }

    private var objectsList = mutableListOf<ObjectViewItem>()

    fun updateData(newSiteAlbumDataList: List<ObjectViewItem>) {
        this.objectsList.setData(newSiteAlbumDataList)
    }

    private fun isDefaultImage(ovi: ObjectViewItem) = ovi.image?.let { false } ?: true

    private fun isVertical(ovi: ObjectViewItem) = ovi.image?.let { it.height >= it.width } ?: false

    private fun isHorizontal(ovi: ObjectViewItem) = ovi.image?.let { it.width > it.height } ?: false

}
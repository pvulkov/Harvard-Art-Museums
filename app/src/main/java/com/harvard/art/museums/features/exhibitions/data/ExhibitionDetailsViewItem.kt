package com.harvard.art.museums.features.exhibitions.data

import android.os.Parcel
import android.os.Parcelable
import com.harvard.art.museums.ext.EMPTY

data class ExhibitionDetailsViewItem(
        val type: ViewItemType = ViewItemType.DATA,
        val title: String? = EMPTY,
        val exhibitionId: Int,
        val imageUrl: String = EMPTY,
        val exhibitionUrl: String? = EMPTY,
        val textileDescription: String? = EMPTY,
        val exhibitionFromTo: String? = EMPTY,
        val people: String? = EMPTY,
        val next: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            ViewItemType.values()[parcel.readInt()],
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type.ordinal)
        parcel.writeString(title)
        parcel.writeInt(exhibitionId)
        parcel.writeString(imageUrl)
        parcel.writeString(exhibitionUrl)
        parcel.writeString(exhibitionFromTo)
        parcel.writeString(textileDescription)
        parcel.writeString(people)
        parcel.writeString(next)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExhibitionDetailsViewItem> {
        override fun createFromParcel(parcel: Parcel): ExhibitionDetailsViewItem {
            return ExhibitionDetailsViewItem(parcel)
        }

        override fun newArray(size: Int): Array<ExhibitionDetailsViewItem?> {
            return arrayOfNulls(size)
        }
    }
}

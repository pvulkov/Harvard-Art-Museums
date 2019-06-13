package com.harvard.art.museums.features.exhibitions.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.harvard.art.museums.ext.EMPTY
import com.harvard.art.museums.ext.setData
import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem
import com.harvard.art.museums.features.exhibitions.data.ViewItemAction
import io.reactivex.subjects.BehaviorSubject


class ExPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val viewEvent = BehaviorSubject.create<ViewItemAction>()


    private val exhibitionViewItems = mutableListOf<ExhibitionDetailsViewItem>()

    fun updateData(newViewItems: List<ExhibitionDetailsViewItem>) {
        exhibitionViewItems.setData(newViewItems)
        notifyDataSetChanged()
    }


    fun loadMoreEvent() = viewEvent


    // Returns total number of pages
    override fun getCount() = exhibitionViewItems.size

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        return ExhibitionFragment.newInstance(exhibitionViewItems[position])
    }

    //TODO (pvalkov) check if code below is used
    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }

}
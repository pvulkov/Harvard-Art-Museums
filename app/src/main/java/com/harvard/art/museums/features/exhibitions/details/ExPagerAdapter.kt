package com.harvard.art.museums.features.exhibitions.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ExPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 // Fragment # 0 - This will show FirstFragment
            -> return ExhibitionFragment.newInstance("Page # 1")
            1 // Fragment # 0 - This will show FirstFragment different title
            -> return ExhibitionFragment.newInstance("Page # 2")
            2 // Fragment # 1 - This will show SecondFragment
            -> return ExhibitionFragment.newInstance("Page # 3")
            else -> return null
        }
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }

    companion object {
        private val NUM_ITEMS = 3
    }

}
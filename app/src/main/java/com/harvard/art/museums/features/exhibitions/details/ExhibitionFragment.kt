package com.harvard.art.museums.features.exhibitions.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.harvard.art.museums.R
import com.harvard.art.museums.ext.isValidUrl
import com.harvard.art.museums.features.exhibitions.data.ExhibitionDetailsViewItem
import kotlinx.android.synthetic.main.fragment_exibition.*


class ExhibitionFragment : Fragment() {

    private lateinit var item: ExhibitionDetailsViewItem


    companion object {

        private const val KEY = "ExhibitionDetailsViewItem"

        // newInstance constructor for creating fragment with arguments
        fun newInstance(item: ExhibitionDetailsViewItem): ExhibitionFragment {
            val fragmentFirst = ExhibitionFragment()
            val args = Bundle()
            args.putParcelable(KEY, item)
            fragmentFirst.setArguments(args)
            return fragmentFirst
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_exibition, container, false)
    }


    override fun onResume() {
        super.onResume()
        item = arguments!!.getParcelable(KEY)!!

        if (item.exhibitionUrl.isValidUrl())
            Glide.with(context!!).load(item.imageUrl).centerCrop().into(exhDetailsPoster).waitForLayout()

        exhDetailsDescription.text = item.textileDescription
    }

}
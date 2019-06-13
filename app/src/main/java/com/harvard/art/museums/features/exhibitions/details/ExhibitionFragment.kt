package com.harvard.art.museums.features.exhibitions.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harvard.art.museums.R
import kotlinx.android.synthetic.main.fragment_exibition.*


class ExhibitionFragment : Fragment() {

    private var title: String? = null


    companion object {

        // newInstance constructor for creating fragment with arguments
        fun newInstance( title: String): ExhibitionFragment {
            val fragmentFirst = ExhibitionFragment()
            val args = Bundle()
            args.putString("someTitle", title)
            fragmentFirst.setArguments(args)
            return fragmentFirst
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view =  inflater.inflate(R.layout.fragment_exibition, container, false)
//        exhibitionNum.text = arguments?.getString("someTitle")

        return view
    }


}
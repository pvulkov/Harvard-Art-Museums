package com.harvard.art.museums.features.objects.details

import com.harvard.art.museums.data.pojo.*

data class ObjectDetailsViewItem(

        val objectNumber: String,
        val people: List<People>,
        val title: String,
        val classification: String,
        val worktypes: List<Worktype>,
        val dated: String,
        val places: List<Place>,
        val culture: String,
        val medium: String,
        val dimensions: String,
        val creditLine: String,
        val accessionyear: Int,
        val division: String,
        val contact: String,
        //TODO (pvalkov) convert to string
        val exhibitions: List<Exhibition>,
        //TODO (pvalkov) convert to string

        val publications: List<Publication>
)
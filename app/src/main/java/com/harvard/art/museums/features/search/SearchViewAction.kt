package com.harvard.art.museums.features.search

import android.view.View
import com.harvard.art.museums.ext.EMPTY


data class SearchViewAction(
        val view: View,
        val action: Action,
        val filter: Filter,
        val text: String? = EMPTY)


enum class Filter { EXHIBITION, OBJECTS, UNKNOWN }

enum class Action { FILTER, SEARCH }



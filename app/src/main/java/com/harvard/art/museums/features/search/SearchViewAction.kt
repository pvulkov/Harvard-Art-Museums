package com.harvard.art.museums.features.search

import com.harvard.art.museums.ext.EMPTY

data class SearchViewAction(
        val action: Action,
        val filter: Filter,
        val text: String? = EMPTY,
        val isSubmitted: Boolean = false)


enum class Filter { EXHIBITION, OBJECTS, UNKNOWN }

enum class Action { ACTION_FILTER, ACTION_SEARCH, ACTION_INIT }



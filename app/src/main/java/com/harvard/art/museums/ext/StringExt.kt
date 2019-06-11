package com.harvard.art.museums.ext

import android.webkit.URLUtil


const val EMPTY: String = ""

fun String?.isValidUrl() = this?.let { URLUtil.isValidUrl(this) } ?: false
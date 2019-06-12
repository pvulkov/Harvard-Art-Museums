package com.harvard.art.museums.ext

import android.webkit.URLUtil
import java.text.SimpleDateFormat
import java.util.*


const val EMPTY: String = ""

private var SERVER_SDF = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault())


fun String?.isValidUrl() = this?.let { URLUtil.isValidUrl(this) } ?: false


/**
 * Parses strings like "2020-01-05" to date object
 * */
fun String.fromServerDate(): Date  = SERVER_SDF.parse(this)

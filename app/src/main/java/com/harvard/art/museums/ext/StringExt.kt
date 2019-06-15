package com.harvard.art.museums.ext

import android.os.Build
import android.text.Html
import android.webkit.URLUtil
import com.harvard.art.museums.THUMB_SIZE
import java.text.SimpleDateFormat
import java.util.*


const val EMPTY: String = ""

private var SERVER_SDF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


fun String?.isValidUrl() = this?.let { URLUtil.isValidUrl(this) } ?: false


/**
 * Parses strings like "2020-01-05" to date object
 * */
fun String.fromServerDate(): Date = SERVER_SDF.parse(this)


@SuppressWarnings("All")
fun String?.fromHtml(): String = this?.let {

    return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        true -> Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
        false -> Html.fromHtml(this).toString()
    }
} ?: EMPTY


fun String.thumbUrl() = if (this.isValidUrl()) "$this?height=$THUMB_SIZE&width=$THUMB_SIZE" else this

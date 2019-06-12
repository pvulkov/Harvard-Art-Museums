package com.harvard.art.museums.ext

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment


fun Fragment.generateShareIntent(url: String) = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, url)
    type = "text/plain"
}


fun Fragment.generateViewIntent(url: String) = Intent().apply {
    action = Intent.ACTION_VIEW
    data = Uri.parse(url)
}
package com.harvard.art.museums.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment


fun generateShareIntent(url: String) = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, url)
    type = "text/plain"
}


fun generateViewIntent(url: String) = Intent().apply {
    action = Intent.ACTION_VIEW
    data = Uri.parse(url)
}


fun Fragment.generateActivityIntent(klass: Class<out Activity>, bundle: Bundle? = null) =
        Intent(this.context, klass).apply {
            bundle?.let { putExtras(it) }
        }

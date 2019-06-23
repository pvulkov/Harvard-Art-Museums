package com.harvard.art.museums.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard() {

    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = this.currentFocus
    if (view == null) {
        view = View(this)
    }

    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Activity.generateActivityIntent(klass: Class<out Activity>, bundle: Bundle? = null) =
        Intent(this, klass).apply { bundle?.let { putExtras(it) } }
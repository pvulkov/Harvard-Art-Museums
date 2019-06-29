package com.harvard.art.museums.ext

import android.view.View
import android.widget.TextView

fun View.show() {
    this.visibility = View.VISIBLE
}


fun View.hide() {
    this.visibility = View.GONE
}


/**
 * set text and visibility to a text view
 * if text is null or empty text view visibility is set to gone
 * */
fun TextView.applyTextAndVisibility(text: String?) {

    text.isNullOrEmpty().ifTrue {
        this.hide()
    }.ifFalse {
        this.show()
        this.text = text
    }

}
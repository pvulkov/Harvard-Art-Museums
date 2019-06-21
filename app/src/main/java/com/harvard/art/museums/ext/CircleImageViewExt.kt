package com.harvard.art.museums.ext

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.floatingactionbutton.FloatingActionButton


fun FloatingActionButton.setColor(@ColorInt color: Int) {
    this.backgroundTintList = ColorStateList.valueOf(color)
}
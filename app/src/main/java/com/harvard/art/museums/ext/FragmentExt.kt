package com.harvard.art.museums.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.Serializable


fun <K : String, V> generateArguments(k: K, v: V): Bundle {

    val extras = Bundle()
    //NOTE (pvalkov) here we go again...
    when (v) {
        is String -> extras.putString(k, v)
        is Int -> extras.putInt(k, v)
        is IBinder -> extras.putBinder(k, v)
        is Bundle -> extras.putBundle(k, v)
        is Byte -> extras.putByte(k, v)
        is ByteArray -> extras.putByteArray(k, v)
        is Char -> extras.putChar(k, v)
        is CharArray -> extras.putCharArray(k, v)
        is CharSequence -> extras.putCharSequence(k, v)
        is Float -> extras.putFloat(k, v)
        is FloatArray -> extras.putFloatArray(k, v)
        is Parcelable -> extras.putParcelable(k, v)
        is Serializable -> extras.putSerializable(k, v)
        is Short -> extras.putShort(k, v)
        is ShortArray -> extras.putShortArray(k, v)

        else -> throw IllegalArgumentException("Unhandled generateArguments case")
    }

    return extras
}

fun Fragment.showToast(text: String?) = text?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }


fun generateShareIntent(url: String) = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, url)
    type = "text/plain"
}


fun generateViewIntent(url: String) = Intent().apply {
    action = Intent.ACTION_VIEW
    data = Uri.parse(url)
}


fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().apply {
        replace(containerId, fragment)
        commit()
    }
}

fun Fragment.generateActivityIntent(klass: Class<out Activity>, bundle: Bundle? = null) =
        Intent(this.context, klass).apply {
            bundle?.let { putExtras(it) }
        }

package com.harvard.art.museums.ext


inline fun Boolean.ifTrue(body: () -> Unit): Boolean {
    if (this)
        body()

    return this
}


inline fun Boolean.ifFalse(body: () -> Unit): Boolean {
    if (!this)
        body()

    return this
}
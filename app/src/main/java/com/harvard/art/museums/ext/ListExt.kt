package com.harvard.art.museums.ext


fun <T> MutableList<T>.setData(data: List<T>) {
    this.clear()
    this.addAll(data)
}

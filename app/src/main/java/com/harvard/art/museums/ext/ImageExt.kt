package com.harvard.art.museums.ext

import com.harvard.art.museums.data.pojo.Image

fun Image?.hasValidUrl(): Boolean = this?.baseimageurl.isValidUrl()
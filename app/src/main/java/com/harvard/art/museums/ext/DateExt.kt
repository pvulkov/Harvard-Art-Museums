package com.harvard.art.museums.ext

import java.text.SimpleDateFormat
import java.util.*


//TODO (pvalkov) refactor
private var ddMMMMyyyy = SimpleDateFormat("dd, MMMM yyyy", Locale.getDefault())


fun Date.to_ddMMMMyyyy() = ddMMMMyyyy.format(this)
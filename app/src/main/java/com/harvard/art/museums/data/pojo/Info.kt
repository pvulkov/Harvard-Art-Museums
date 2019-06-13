package com.harvard.art.museums.data.pojo

data class Info(
        val page: Int,
        val pages: Int,
        val totalrecords: Int,
        val totalrecordsperquery: Int,
        var cur: String? = null,
        val prev: String? = null,
        val next: String? = null
)

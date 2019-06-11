package com.harvard.art.museums.data.pojo

data class Info(
        val page: Int,
        val pages: Int,
        val totalrecords: Int,
        val totalrecordsperquery: Int,
        val next: String? = null
)

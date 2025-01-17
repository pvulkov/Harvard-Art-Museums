package com.harvard.art.museums.data.pojo

data class Venue(
        val address1: String,
        val address2: String? = null,
        val begindate: String,
        val city: String,
        val country: String,
        val enddate: String,
        val fullname: String,
        val ishamvenue: Int,
        val name: String,
        val state: String,
        val venueid: Int,
        val zipcode: String,
        val galleries: List<Gallery> = listOf()
)
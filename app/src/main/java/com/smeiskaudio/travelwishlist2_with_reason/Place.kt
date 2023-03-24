package com.smeiskaudio.travelwishlist2_with_reason


data class Place(val name: String, val reason: String? = null, var starred: Boolean = false,
            val id: Int? = null)  // Note that all names MUST match the API for Retrofit to work.

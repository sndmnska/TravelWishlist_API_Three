package com.smeiskaudio.travelwishlist2_with_reason

import java.text.SimpleDateFormat
import java.util.*

class Place(val name: String, val dateAdded: Date = Date()) {
    fun formattedDate(): String {
        return SimpleDateFormat("EEE, d, MMM, yyy", Locale.getDefault()).format(dateAdded)
    }

    override fun toString(): String {
        return "$name ${formattedDate()}"
    }
}
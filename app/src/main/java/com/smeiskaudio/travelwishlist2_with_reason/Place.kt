package com.smeiskaudio.travelwishlist2_with_reason

import java.text.SimpleDateFormat
import java.util.*

class Place(val name: String, val reason: String, val dateAdded: Date = Date()) { // added "reason" to the constructor
    fun formattedDate(): String {
        return SimpleDateFormat("EEE, MMM. d, yyy", Locale.getDefault()).format(dateAdded)
    }

    override fun toString(): String { // used in the Delete SnackBar
        return "$name ${formattedDate()}" // Did not add "reason" here, as it is not relevant to deleted information
    }
}
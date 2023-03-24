package com.smeiskaudio.travelwishlist2_with_reason


// status of request - success, server error, network error
// data, if any
// message for user, if needed
enum class ApiStatus {
    SUCCESS,
    SERVER_ERROR,
    NETWORK_ERROR
}

// T = could be a Place, could be a List of Places.
data class ApiResult<out T> (val status: ApiStatus, val data: T?, val message: String?)  // T is a placeholder of a Kotlin type, optional value (here nullable)

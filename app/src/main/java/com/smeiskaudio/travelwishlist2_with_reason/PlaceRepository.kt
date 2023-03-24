package com.smeiskaudio.travelwishlist2_with_reason

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRepository {

    private val TAG = "PLACE_REPOSITORY"

    private val baseURL = "https://claraj.pythonanywhere.com/api/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationHeaderInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val placeService = retrofit.create(PlaceService::class.java)

    suspend fun <T: Any> apiCall( apiCallFunction: suspend () -> Response<T>,
                                  successMessage: String?, failMessage: String?): ApiResult<T> {
        try {
//            val response = placeService.getAllPlaces()
            // replace the function with the function definition that's provided in the argument
            val response = apiCallFunction.invoke() //
            if (response.isSuccessful) { // connected to API server, got data back
                Log.d(TAG, "Response body {${response.body()}}")
                return ApiResult(ApiStatus.SUCCESS, response.body(), successMessage)
            } else { // connected to the server, but server sent an error message
                Log.e(TAG, "Server error -- ${response.errorBody()!!.string()}")
                return ApiResult(ApiStatus.SERVER_ERROR, null, failMessage)
            }
        } catch (ex: Exception) { // can't connect to server - network error
            Log.e(TAG, "Error connecting to API server -- ", ex)
            return ApiResult(ApiStatus.NETWORK_ERROR, null, "Can't connect to server")
        }
    }


    suspend fun getAllPlaces(): ApiResult<List<Place>> {
        return apiCall(placeService::getAllPlaces,
            "Places loaded",
            "Error fetching places for server")
    }

    suspend fun addPlace(place: Place): ApiResult<Place> {
        return apiCall(
            { placeService.addPlace(place) } , // lambda function
        "Place created!",
        "Error adding a place - is name unique?"
        )

    }

    suspend fun updatePlace(place: Place): ApiResult<Place> {

        if (place.id == null) {
            val noIdMessage = "Error -- trying to update place with no id"
            Log.e(TAG, noIdMessage)
            return ApiResult(ApiStatus.SERVER_ERROR, null, noIdMessage)
        } else {
            return apiCall(
                {placeService.updatePlace(place, place.id) },
            "Place updated!",
            "Error updating place \"$place\""
            )
        }

    }

    suspend fun deletePlace(place: Place): ApiResult<String> {
        if (place.id == null) {
            val noIdMessage = "Error -- trying to update place with no id"
            Log.e(TAG, noIdMessage)
            return ApiResult(ApiStatus.SERVER_ERROR, null, noIdMessage)
        } else {
            return apiCall(
                { placeService.deletePlace(place.id) },
                "Place deleted",
                "Error deleting place \"$place\" from list"
            )
        }
    }

    /*Below is the former code for error handling before grouping all of the above into a cleaner
    function using apiCall().  Included here for better understanding and reference.

    *Begin archived code*/

//    suspend fun getAllPlaces(): ApiResult<List<Place>> {
//        try {
//            val response = placeService.getAllPlaces()
//
//            if (response.isSuccessful) { // connected to API server, got data back
//
//                val places = response.body() ?: listOf()
//                Log.d(TAG, "List of places $places")
//                return ApiResult(ApiStatus.SUCCESS, response.body(), null)
//            } else { // connected to the server, but server sent an error message
//                Log.e(
//                    TAG,
//                    "API server connected but returned error -- ${response.errorBody()!!.string()}"
//                )
//                return ApiResult(ApiStatus.SERVER_ERROR, null, "Error fetching places")
//            }
//        } catch (ex: Exception) { // can't connect to server - network error
//            Log.e(TAG, "Error connecting to API server -- ", ex)
//            return ApiResult(ApiStatus.NETWORK_ERROR, null, "Can't connect to server")
//        }

    /*End of archived code*/
}
package com.smeiskaudio.travelwishlist2_with_reason

import retrofit2.Response
import retrofit2.http.*

interface PlaceService {
    @GET("places/")
    suspend fun getAllPlaces(): Response<List<Place>>

    // POST create place
    @POST("places/")
    suspend fun addPlace(@Body place: Place): Response<Place>// place in.  Get response to also know the ID back.

    // PATCH update place  - ID of place, data about the new place. Send in body of request
    @PATCH("places/{id}/")
    suspend fun updatePlace(@Body place: Place, @Path("id")id: Int): Response<Place>

    // delete place
    @DELETE("places/{id}/")
    suspend fun deletePlace(@Path("id") id: Int): Response<String>
}
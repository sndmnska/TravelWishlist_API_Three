package com.smeiskaudio.travelwishlist2_with_reason

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//const val TAG = "PLACES_VIEW_MODEL"

class PlacesViewModel : ViewModel() {

    private val placeRepository = PlaceRepository()

    val allPlaces = MutableLiveData<List<Place>>(listOf<Place>())
    val userMessage = MutableLiveData<String>(null)

    init {
        getPlaces()
    }

    fun getPlaces() {
        viewModelScope.launch {// a coroutine calls this in the background. //  try to understand this better...
//            allPlaces.postValue(placeRepository.getAllPlaces()) // this is a suspend function and needs to be called int he background
            // alternative to writing the above code. Both are correct.
            val apiResult = placeRepository.getAllPlaces()
            if (apiResult.status == ApiStatus.SUCCESS) {
                allPlaces.postValue(apiResult.data)
            } else {
                userMessage.postValue(apiResult.message)
            }
//            allPlaces.postValue(places)  // from older code where we were sending "places" not "apiResult"
        }
    }

    fun addNewPlace(place: Place) { // :return data type
        viewModelScope.launch {

            val apiResult = placeRepository.addPlace(place)
            refreshListIfSuccessful(apiResult)
        }
    }

    fun updatePlace(place: Place) {
        viewModelScope.launch {
            val apiResult = placeRepository.updatePlace(place)
            refreshListIfSuccessful(apiResult)
        }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch {
            val apiResult = placeRepository.deletePlace(place)
            refreshListIfSuccessful(apiResult)
        }
    }

    private fun refreshListIfSuccessful(apiResult: ApiResult<Any>) {
        if (apiResult.status == ApiStatus.SUCCESS) {
            getPlaces()
        }
        userMessage.postValue(apiResult.message) // assumes all options will display a message
    }


}

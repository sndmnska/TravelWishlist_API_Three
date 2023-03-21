package com.smeiskaudio.travelwishlist2_with_reason

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

const val TAG = "PLACES_VIEW_MODEL"

class PlacesViewModel : ViewModel() {

    // begin with list of places, example data in parenthesis for easier testing

    private val places =
        mutableListOf<Place>(
            Place("London, UK", "Never been there, always wanted to visit."),
            Place("Veldhoven, NL", "Family lives nearby"),
            Place("Milwaukee, WI, USA", "More family lives nearby")
        ) // let user add places

    // enable access to list
    fun getPlaces(): List<Place> {
        // mutableList is a type of List
        return places // <- smart cast mutableList as a List
    }

    fun addNewPlace(place: Place, position: Int? = null): Int { // :return data type

        /*Classic for loop*/

        // return location in the list that the new item was added
//        for (placeName in placeNames) {
//            if (placeName.uppercase() == place.uppercase()) {
//                // a list item integer is valid if 0 or larger.
//                // -1 indicates a place was not added in this program
//                //      (not a valid index, yes a valid integer)
//                return -1


        /* Using .all or .any */

        // 'all' function returns true if all of the things in a list meet a condition
        // 'any' function returns true if any of the things in a list meets a condition.
        // avoid duplicates
        // return with position
        if (places.any { placeName -> placeName.name.uppercase() == place.name.uppercase() })
        // using 'it' is the default variable for looped tests.  Removes the need for the arrow function.
        // can also do (placeNames.any { it.uppercase() == place.uppercase } )
        {
            return -1
        }
        return if (position == null) {
            // unitVariable -> unitVariable.uppercase() == unitVariable.uppercase()
            places.add(place) // adds at the end
            places.lastIndex
        } else {
            places.add(position, place)
            return position
        }
    }

    fun movePlace(from: Int, to: Int) {
        val place = places.removeAt(from)
        places.add(to, place)
        Log.d(TAG, places.toString())
    }

    fun deletePlace(position: Int): Place {
        return places.removeAt(position)
    }

    fun updatePlace(place: Place) {
        // todo
    }
}


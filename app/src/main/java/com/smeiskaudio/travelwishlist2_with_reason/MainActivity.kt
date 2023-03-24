package com.smeiskaudio.travelwishlist2_with_reason

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.smeiskaudio.travelwishlist.R

class MainActivity : AppCompatActivity(),
    OnListItemClickedListener, OnDataChangedListener {

    private lateinit var newPlaceEditText: EditText
    private lateinit var addNewPlaceButton: Button
    private lateinit var placeListRecyclerView: RecyclerView
    private lateinit var reasonToGoEditText: EditText
    private lateinit var wishlistContainer: View

    private lateinit var placesRecyclerAdapter: PlaceRecyclerAdapter

    private val placesViewModel: PlacesViewModel by lazy {
        ViewModelProvider(this).get(PlacesViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidgets()

        val listener = OnListItemSwipeListener(this)
//        val itemTouchHelper = ItemTouchHelper(listener)
//        itemTouchHelper.attachToRecyclerView(placeListRecyclerView)
        // same as above, simplified
        ItemTouchHelper(listener).attachToRecyclerView(placeListRecyclerView)

//        val places = placesViewModel.getPlaces() // list of place objects

        placesRecyclerAdapter = PlaceRecyclerAdapter(listOf(), this) // this expects a list of String
        placeListRecyclerView.layoutManager = LinearLayoutManager(this)
        placeListRecyclerView.adapter = placesRecyclerAdapter

        addNewPlaceButton.setOnClickListener {
            addNewPlace()
        }

        placesViewModel.allPlaces.observe(this) {places ->
            placesRecyclerAdapter.places = places
            placesRecyclerAdapter.notifyDataSetChanged()
        }

        placesViewModel.userMessage.observe(this) { message ->
            if (message != null) {
                Snackbar.make(wishlistContainer, message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun initWidgets() {
        placeListRecyclerView = findViewById(R.id.place_list)
        addNewPlaceButton = findViewById(R.id.add_new_place_button)
        newPlaceEditText = findViewById(R.id.new_place_name)
        reasonToGoEditText = findViewById(R.id.reason_for_going)
        wishlistContainer = findViewById(R.id.wishlist_container)
    }

    private fun addNewPlace() {
        // I want the "reason" field to be optional.   Leaving in commented out code to allow this to be reverted.
        val name = newPlaceEditText.text.toString().trim()
//        val reason = reasonToGoEditText.text.toString().trim()  // to utilize the reason field

        when {
//            name.isEmpty() && reason.isEmpty() -> {
//                Toast.makeText(
//                    this,
//                    getString(R.string.enter_text_in_both_fields),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
            name.isEmpty() -> {
                Toast.makeText(
                    this,
                    getString(R.string.enter_a_place_name),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
//            reason.isEmpty() -> {
//                Toast.makeText(
//                    this,
//                    getString(R.string.enter_a_reason),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
            else -> {
                val reason = reasonToGoEditText.text.toString()
                val newPlace = Place(name, reason)
                placesViewModel.addNewPlace(newPlace)
//                if (positionAdded == -1) {
//                    Toast.makeText(this, getString(R.string.duplicate_entry), Toast.LENGTH_SHORT).show()
//                } else {
//                placesRecyclerAdapter.notifyItemInserted(positionAdded)
                clearForm()
                hideKeyboard()
//            }
            }
        }

    }

    private fun clearForm() {
        newPlaceEditText.text.clear()
        reasonToGoEditText.text.clear() // clear text from reason EditText
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onMapRequestButtonClicked(place: Place) { // override means coming from an interface
        Toast.makeText(this, "${place.name} map icon was clicked", Toast.LENGTH_SHORT).show()
        val placeLocationUri = Uri.parse("geo:0,0?q=${place.name}")
        val mapIntent = Intent(Intent.ACTION_VIEW, placeLocationUri)
        startActivity(mapIntent)
    }

    override fun onStarredStatusChanged(place: Place, isStarred: Boolean) {
        place.starred = isStarred
        placesViewModel.updatePlace(place)
    }


    override fun onListItemDeleted(position: Int) {

        val placeInt = placesRecyclerAdapter.places[position]
        placesViewModel.deletePlace(placeInt)
    }
    }
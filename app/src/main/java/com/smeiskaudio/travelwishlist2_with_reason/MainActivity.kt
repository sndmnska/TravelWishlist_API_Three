package com.smeiskaudio.travelwishlist2_with_reason

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
    OnListItemClickedListener, OnDateChangedListener {

    private lateinit var newPlaceEditText: EditText
    private lateinit var addNewPlaceButton: Button
    private lateinit var placeListRecyclerView: RecyclerView

    private lateinit var placesRecyclerAdapter: PlaceRecyclerAdapter

    private val placesViewModel: PlacesViewModel by lazy {
        ViewModelProvider(this).get(PlacesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidgets()

        val listener = OnListItemSwipeListener(this)
//        val itemTouchHelper = ItemTouchHelper(listener)
//        itemTouchHelper.attachToRecyclerView(placeListRecyclerView)
        // same as above, simplified
        ItemTouchHelper(listener).attachToRecyclerView(placeListRecyclerView)

        val places = placesViewModel.getPlaces() // list of place objects

        placesRecyclerAdapter = PlaceRecyclerAdapter(places, this) // this expects a list of String
        placeListRecyclerView.layoutManager = LinearLayoutManager(this)
        placeListRecyclerView.adapter = placesRecyclerAdapter

        addNewPlaceButton.setOnClickListener {
            addNewPlace()
        }
    }

    private fun initWidgets() {
        placeListRecyclerView = findViewById(R.id.place_list)
        addNewPlaceButton = findViewById(R.id.add_new_place_button)
        newPlaceEditText = findViewById(R.id.new_place_name)
    }

    private fun addNewPlace() {
        val name = newPlaceEditText.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.enter_a_place_name),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            val newPlace = Place(name)
            val positionAdded = placesViewModel.addNewPlace(newPlace)
            if (positionAdded == -1) {
                Toast.makeText(
                    this,
                    getString(R.string.duplicate_entry),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                placesRecyclerAdapter.notifyItemInserted(positionAdded)
                clearForm()
                hideKeyboard()
            }
        }

    }

    private fun clearForm() {
        newPlaceEditText.text.clear()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onListItemClicked(place: Place) { // override means coming from an interface
        Toast.makeText(this, "${place.name} map icon was clicked", Toast.LENGTH_SHORT).show()
        val placeLocationUri = Uri.parse("geo:0,0?q=${place.name}")
        val mapIntent = Intent(Intent.ACTION_VIEW, placeLocationUri)
        startActivity(mapIntent)
    }

    override fun onListItemMoved(from: Int, to: Int) {
        placesViewModel.movePlace(from, to) // removes the entry from the view model
        placesRecyclerAdapter.notifyItemMoved(
            from,
            to
        ) // tells the recycler adapter about the change
    }

    override fun onListItemDeleted(position: Int) {
        val deletedPlace = placesViewModel.deletePlace(position)
        placesRecyclerAdapter.notifyItemRemoved(position)

        Snackbar.make(
            findViewById(R.id.wishlist_container),
            getString(R.string.confirmation_place_deleted, deletedPlace.name), 5000
        )
            .setActionTextColor(getColor(R.color.red))
            .setBackgroundTint(getColor(R.color.snack_background))
            .setTextColor(getColor(R.color.text_color_dark_green))
            .setAction(getString(R.string.undo)) {// display an "UNDO" button
                placesViewModel.addNewPlace(deletedPlace, position)
                placesRecyclerAdapter.notifyItemInserted(position)
            }
            .show()
    }
}
package com.smeiskaudio.travelwishlist2_with_reason

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smeiskaudio.travelwishlist.R

interface OnListItemClickedListener {
    fun onListItemClicked(place: Place)
}

/** You'll see a lot of red until you're done with the Adapter */


class PlaceRecyclerAdapter(
    private val places: List<Place>,
    private val onListItemClickedListener: OnListItemClickedListener
) :
    RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder>()     // extends this

{
    // the term for data plus layout is ViewHolder
    // within adapter class we need to create a ViewHolder subclass

    // manages one view - one list item - sets the actual data in the view

    // inner classes - add 'inner'

    // nested classes - this is one - self-contained thing
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(place: Place) {
            val placeNameTextView: TextView = view.findViewById(R.id.place_name)
            placeNameTextView.text = place.name

            // add a TextView to bind the reason to the RecyclerView.ViewHolder
            val reasonTextView: TextView = view.findViewById(R.id.reason_textview)
            reasonTextView.text = place.reason

            // textViews have to display strings
            val dateCreatedOnTextView: TextView = view.findViewById(R.id.date_place_added)
            val createdOnText = view.context.getString(R.string.created_on, place.formattedDate())
            dateCreatedOnTextView.text = createdOnText

            val mapIcon: ImageView = view.findViewById(R.id.map_icon)
            mapIcon.setOnClickListener {
                // what now?
                onListItemClickedListener.onListItemClicked(place)
            }

        }
    }


    // create a ViewHolder for a specific position? (combo view + data)

    // called by the recycler view to create as many view holders that are needed to
    // display the list on screen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.place_list_item,
                parent,
                false
            ) // attachToRoot means is it on a static place on the whole screen
        return ViewHolder(view)
    }

    // bind the ViewHolder with data for a specific position
    // called by the recycler view to set the data for one list item, by position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position] // get the data that should be displayed ina specific position.
        holder.bind(place)
    }

    // how many items in the list?
    override fun getItemCount(): Int {
        return places.size
    }
}






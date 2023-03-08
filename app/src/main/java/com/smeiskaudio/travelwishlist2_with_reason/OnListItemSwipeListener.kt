package com.smeiskaudio.travelwishlist2_with_reason

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// create an interface, like in RecyclerAdapter
interface OnDateChangedListener {
    fun onListItemMoved(from: Int, to: Int)  // move up and down in a list
    fun onListItemDeleted(position: Int)

}

class OnListItemSwipeListener(private val onDataChangedListener: OnDateChangedListener) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, // to reorder
        ItemTouchHelper.RIGHT // to delete
    ) {
    override fun onMove( // moving up and down
        recyclerView: RecyclerView, // the recyclerview itself, not used here
        viewHolder: RecyclerView.ViewHolder, // ?reference to the content being displayed
        target: RecyclerView.ViewHolder
    ): Boolean {
        // where did it move from, and where to?
        val fromPosition = viewHolder.adapterPosition // where is it on the list?
        val toPosition = target.adapterPosition // going to where?
        onDataChangedListener.onListItemMoved(fromPosition, toPosition)
        return true // is the move permitted?
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) { // swiping left and swiping right
        if (direction == ItemTouchHelper.RIGHT) {
            onDataChangedListener.onListItemDeleted(viewHolder.adapterPosition)
        }
    }
}
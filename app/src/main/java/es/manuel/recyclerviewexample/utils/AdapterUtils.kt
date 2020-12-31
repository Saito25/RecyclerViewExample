package es.manuel.recyclerviewexample.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.doOnSwipe(
    swipeDirection: Int = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT,
    onSwipeAction: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit
) {
    val itemTouchHelper = ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSwipeAction(viewHolder, direction)
        }
    })
    itemTouchHelper.attachToRecyclerView(this)
}


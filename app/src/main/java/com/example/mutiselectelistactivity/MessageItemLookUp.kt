package com.example.mutiselectelistactivity

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MessageItemLookUp(val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val vh = recyclerView.getChildViewHolder(view)
            if (vh is SelectableListAdapter.SelectableListVH) {
                return vh.getMessageDetail()
            }
        }
        return null
    }

}
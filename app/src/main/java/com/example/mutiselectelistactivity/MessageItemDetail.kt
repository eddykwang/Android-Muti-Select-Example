package com.example.mutiselectelistactivity

import androidx.recyclerview.selection.ItemDetailsLookup

class MessageItemDetail(var key: Long, var pos: Int) : ItemDetailsLookup.ItemDetails<Long>() {

    override fun getSelectionKey(): Long? {
        return key
    }

    override fun getPosition(): Int {
        return pos
    }

}
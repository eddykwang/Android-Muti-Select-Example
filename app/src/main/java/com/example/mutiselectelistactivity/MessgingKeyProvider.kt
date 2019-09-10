package com.example.mutiselectelistactivity

import androidx.recyclerview.selection.ItemKeyProvider

class MessgingKeyProvider(val list: List<MessageModel>) : ItemKeyProvider<String>(SCOPE_CACHED) {

    private val map: HashMap<String, Int> = HashMap()

    init {
        var i = 0
        for (m in list) {
            map[m.title] = i
        }
    }

    override fun getKey(position: Int): String? {
        return list[position].title
    }

    override fun getPosition(key: String): Int {
        return map[key]!!
    }

}
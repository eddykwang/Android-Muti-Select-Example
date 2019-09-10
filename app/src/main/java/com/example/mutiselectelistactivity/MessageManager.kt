package com.example.mutiselectelistactivity

class MessageManager {
    companion object {
        fun getMessage(page: Int): ArrayList<MessageModel> {
            return ArrayList<MessageModel>().apply {
                for (i in page * 20+1..page * 20 + 20) {
                    add(MessageModel("Name $i", "Title $i", "https://picsum.photos/id/${i * 2}/300/300"))
                }
            }
        }
    }
}
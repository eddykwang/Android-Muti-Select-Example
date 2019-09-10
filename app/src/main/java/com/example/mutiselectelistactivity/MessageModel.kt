package com.example.mutiselectelistactivity

data class MessageModel(
    var name: String,
    var title: String = "default title",
    var imageUrl: String? = null,
    var content: String = "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit..."
)

package ru.gms.mosopencontrol.model.entity

data class ChatMessage(
    val text: String,
    val timeStamp: String,
    val isFromUser: Boolean,
    val isReceived: Boolean,
)

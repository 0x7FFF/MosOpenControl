package ru.gms.mosopencontrol.ui.screens.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.utils.onError
import io.getstream.chat.android.client.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.gms.mosopencontrol.model.entity.ChatMessage
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class ChatViewModel @Inject constructor() : ViewModel() {
    val messages = mutableStateOf<List<ChatMessage>>(listOf(
        ChatMessage("Hello", isFromUser = false, timeStamp = "13:38", isReceived = true),
        ChatMessage("Hello", isFromUser = true, timeStamp = "13:39", isReceived = true),
    ))

    val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun onSendMessage(message: String) {
        val timestamp = LocalTime.now().format(dateTimeFormatter)
        messages.value = messages.value + ChatMessage(
            message,
            isFromUser = true,
            isReceived = true,
            timeStamp = timestamp.toString()
        )
    }

    fun onReceiveMessage(message: String) { //TODO: Replace with real data instead of str.
        val timestamp = LocalTime.now().format(dateTimeFormatter)
        messages.value = messages.value + ChatMessage(message,
            isFromUser = false,
            isReceived = true,
            timeStamp = timestamp.toString()
        )
    }

    fun onAttachFile() {
        val timestamp = LocalTime.now().format(dateTimeFormatter)
        messages.value = messages.value + ChatMessage(
            "Файл",
            isFromUser = true,
            isReceived = true,
            timeStamp = timestamp.toString()
        )

    }

    fun onStartVoiceMessage() {
        val timestamp = LocalTime.now().format(dateTimeFormatter)
        messages.value = messages.value + ChatMessage(
            "Голосовое сообщение",
            isFromUser = true,
            isReceived = true,
            timeStamp = timestamp.toString()
        )
    }
}

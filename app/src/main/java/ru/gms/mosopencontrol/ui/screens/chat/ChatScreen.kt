@file:OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)

package ru.gms.mosopencontrol.ui.screens.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.model.entity.ChatMessage
import ru.gms.mosopencontrol.ui.component.button.ButtonColors
import ru.gms.mosopencontrol.ui.component.button.LargeButton
import ru.gms.mosopencontrol.ui.component.button.TextButtonViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    navController: NavController,
) {
    var messageText by remember { mutableStateOf("") }
    val messagesLocal by remember { viewModel.messages }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MosOpenControlTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            ChatHeader {
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (viewModel.messages.value.isEmpty()) {
                Column(
                    modifier = Modifier.padding(horizontal = 36.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    InitialMessageCard()
                    Spacer(modifier = Modifier.weight(1f))
                    LargeButton(
                        state = TextButtonViewState(
                            text = stringResource(id = R.string.chat_new_go)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colorScheme = ButtonColors.onPrimaryColorScheme(),
                    ) {
                        viewModel.onSendMessage("Начать")
                    }
                }
            } else {
                MessageList(
                    messages = messagesLocal,
                    modifier = Modifier.weight(1f)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                BottomBar(
                    messageText = messageText,
                    onMessageTextChanged = { messageText = it },
                    onSendMessage = {
                        viewModel.onSendMessage(it)
                        messageText = ""
                    },
                    onAttachFile = viewModel::onAttachFile,
                    onStartVoiceMessage = viewModel::onStartVoiceMessage,
                )
            }
        }
    }
}

@Composable
fun MessageList(modifier: Modifier, messages: List<ChatMessage>) {
    LazyColumn(modifier = modifier) {
        items(items = messages) { message ->
            TextMessage(message = message)
        }
    }
}

@Composable
fun TextMessage(message: ChatMessage) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isFromUser) Color(0xFFF64027) else Color(0xFFF7F7F7)
    val textColor = if (message.isFromUser) Color.White else Color(0xFF112B27)
    val timeStampColor = if (message.isFromUser) Color(0xFFFFFFFF) else Color(0x2125294D)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 9.dp, horizontal = 12.dp)
                .clip(RoundedCornerShape(16.dp)),
            horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
        ) {
            Surface(
                color = bubbleColor,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.titleMedium.copy(color = textColor),
                        modifier = Modifier.padding(start = 12.dp, top = 9.dp, end = 12.dp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 13.dp, end = 8.dp, bottom = 8.dp),
                        text = message.timeStamp,
                        style = MaterialTheme.typography.titleSmall.copy(color = timeStampColor)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    messageText: String,
    onMessageTextChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onAttachFile: () -> Unit,
    onStartVoiceMessage: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onAttachFile
        ) {
            Icon(
                painterResource(id = R.drawable.ic_chat_attach),
                contentDescription = "Attach file"
            )
        }

        TextField(
            value = messageText,
            onValueChange = onMessageTextChanged,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused.value = it.isFocused }
                .background(MaterialTheme.colorScheme.onPrimary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF212529),
                unfocusedTextColor = Color(0xFF212529),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )

        IconButton(
            onClick = {
                if (isFocused.value && messageText.isNotEmpty()) {
                    onSendMessage(messageText)
                } else if (!isFocused.value) {
                    onStartVoiceMessage()
                }
            }
        ) {
            if (isFocused.value && messageText.isNotEmpty()) {
                Icon(
                    painterResource(id = R.drawable.ic_chat_send),
                    contentDescription = "Send message"
                )
            } else {
                Icon(
                    painterResource(id = R.drawable.ic_chat_mic),
                    contentDescription = "Start voice message"
                )
            }
        }
    }
}

package ru.gms.mosopencontrol.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHeader(onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = MosOpenControlTheme.colorScheme.primary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MosOpenControlTheme.colorScheme.background,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1.0f),
                ) {
                    Text(
                        state = TextViewState(
                            style = MosOpenControlTheme.typography.titleMedium,
                            text = stringResource(id = R.string.chat_title),
                            color = MosOpenControlTheme.colorScheme.secondary,
                        )
                    )
                    Text(
                        state = TextViewState(
                            style = MosOpenControlTheme.typography.titleSmall,
                            text = stringResource(id = R.string.chat_subtitle),
                            color = MosOpenControlTheme.colorScheme.surfaceVariant,
                        )
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_chat_bot_avatar),
                    contentDescription = "Chatbot avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }
        }
    )
}

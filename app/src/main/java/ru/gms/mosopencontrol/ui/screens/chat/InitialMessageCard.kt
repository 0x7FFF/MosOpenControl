package ru.gms.mosopencontrol.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

@Composable
fun InitialMessageCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .height(334.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_chatbot_welcome),
                contentDescription = "Initial message image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(319f / 149f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    state = TextViewState(
                        style = MosOpenControlTheme.typography.titleLarge,
                        text = stringResource(id = R.string.chat_new_subtitle),
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    state = TextViewState(
                        text = stringResource(id = R.string.chat_new_description),
                        style = MosOpenControlTheme.typography.bodyMedium
                    )
                )
            }
        }
    }
}

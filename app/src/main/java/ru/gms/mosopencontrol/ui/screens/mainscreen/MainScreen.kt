package ru.gms.mosopencontrol.ui.screens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.model.entity.MainListItem
import ru.gms.mosopencontrol.model.entity.MainListItemAction
import ru.gms.mosopencontrol.model.entity.MOSUser
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

@Composable
fun MainScreen(user: MOSUser, listItems: List<MainListItem>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Header(user = user)
        Spacer(modifier = Modifier.height(21.dp))
        ListItemContent(listItems = listItems) { action ->
            when (action) {
                is MainListItemAction.Chatbot -> {
                    //Add navigation here!
                }
                is MainListItemAction.Consultation -> {
                    //Add intent to other app.
                }
            }
        }
    }
}

@Composable
fun Header(user: MOSUser) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                state = TextViewState(
                    style = MosOpenControlTheme.typography.bodyMedium,
                    text = "Добрый вечер,",
                    color = Color(0xff3C514E), //TODO: Add from DS.
                    numberOfLines = 1,
                )
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                state = TextViewState(
                    style = MosOpenControlTheme.typography.headlineSmall,
                    text = user.name,
                    color = Color(0xff112B27), //TODO: Add from DS.
                    numberOfLines = 1,
                )
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        ) {
            if (user.avatarResId != null) {
                Image(
                    painter = painterResource(id = user.avatarResId),
                    contentDescription = "User avatar",
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFDF5F2)), // TODO: Add from DS
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        state = TextViewState(
                            style = MosOpenControlTheme.typography.bodyMedium,
                            text = user.name.first().uppercase() + user.surname.first().uppercase(),
                            color = MosOpenControlTheme.colorScheme.primary,
                            numberOfLines = 1,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ListItemContent(listItems: List<MainListItem>, onListItemAction: (MainListItemAction) -> Unit) {
    LazyColumn {
        items(items = listItems) { listItem ->
            ListItemCard(listItem = listItem, onListItemAction = onListItemAction)
        }
    }
}

@Composable
fun ListItemCard(listItem: MainListItem, onListItemAction: (MainListItemAction) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onListItemAction(listItem.action) },
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
            ) {
                Text(
                    state = TextViewState(
                        style = MosOpenControlTheme.typography.headlineSmall,
                        text = listItem.header,
                        color = MosOpenControlTheme.colorScheme.onTertiary,
                        numberOfLines = 1,
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    state = TextViewState(
                        style = MosOpenControlTheme.typography.bodyMedium,
                        text = listItem.text,
                        color = MosOpenControlTheme.colorScheme.surfaceVariant,
                    )
                )
            }
            listItem.imageRes?.let {
                Image(
                    painter = painterResource(it),
                    modifier = Modifier
                        .width(198.dp)
                        .height(99.dp),
                    contentDescription = "Chatbot",
                )
            }
        }
    }
}

//region preview

@Preview
@Composable
fun PreviewMainScreen() {
    MosOpenControlTheme {
        MainScreen(
            user = MOSUser(
                name = "Дмитрий",
                surname = "Сохин",
                avatarResId = null
            ),
            listItems = listOf(
                MainListItem(
                    header = "Чат-бот",
                    text = "Поможет ответить на \n" +
                            "вопросы по бизнесу",
                    imageRes = R.drawable.img_chatbot,
                    action = MainListItemAction.Consultation
                ),
                MainListItem(
                    header = "Консультация",
                    text = "Мы ответим на все вопросы\n" +
                            "по бизнесу и товарам",
                    imageRes = R.drawable.img_consultation,
                    action = MainListItemAction.Consultation
                )
            )
        )
    }
}

//endregion

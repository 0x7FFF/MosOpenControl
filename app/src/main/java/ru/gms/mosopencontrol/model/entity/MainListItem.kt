package ru.gms.mosopencontrol.model.entity

import androidx.annotation.DrawableRes

data class MainListItem(
    val header: String,
    val text: String,
    @DrawableRes val imageRes: Int? = null,
    val action: MainListItemAction,
)

sealed class MainListItemAction {
    data class Chatbot(val userId: Int) : MainListItemAction()
    object Consultation : MainListItemAction()
}

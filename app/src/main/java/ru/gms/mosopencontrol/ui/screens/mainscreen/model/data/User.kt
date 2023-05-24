package ru.gms.mosopencontrol.ui.screens.mainscreen.model.data

import androidx.annotation.DrawableRes
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val surname: String,
    @DrawableRes val avatarResId: Int?,
)


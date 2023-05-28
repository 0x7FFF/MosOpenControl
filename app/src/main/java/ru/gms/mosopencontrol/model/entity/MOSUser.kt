package ru.gms.mosopencontrol.model.entity

import androidx.annotation.DrawableRes
import java.util.UUID

data class MOSUser(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val surname: String,
    @DrawableRes val avatarResId: Int?,
)


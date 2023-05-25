package ru.gms.mosopencontrol.ui.utils

import androidx.compose.ui.graphics.Color

internal fun Color.toDisabledContainerColor(): Color =
    this.copy(alpha = .50f)

internal fun Color.toDisabledContentColor(): Color =
    this.copy(alpha = .50f)

internal fun Color.toPressedContentColor(): Color =
    this.copy(alpha = .50f)

internal fun Color.toShadowColor(): Color =
    this.copy(alpha = .12f)

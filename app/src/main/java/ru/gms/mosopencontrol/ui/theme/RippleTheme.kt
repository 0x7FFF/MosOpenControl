package ru.gms.mosopencontrol.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object DefaultRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color =
        MosOpenControlTheme.colorScheme.onBackground

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            contentColor = defaultColor(),
            lightTheme = !isSystemInDarkTheme()
        )
}

object TransparentRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color =
        MosOpenControlTheme.colorScheme.background

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleAlpha(
            draggedAlpha = 0f,
            focusedAlpha = 0f,
            hoveredAlpha = 0f,
            pressedAlpha = 0f
        )
}

package ru.gms.mosopencontrol.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf


@Composable
fun MosOpenControlTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme() {
        CompositionLocalProvider(
            LocalRippleTheme provides DefaultRippleTheme,
            LocalAppColorScheme provides LightColorScheme,
            LocalAppTypography provides Typography,
            content = content
        )
    }
}

object MosOpenControlTheme {
    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}

private val LocalAppColorScheme = staticCompositionLocalOf<AppColorScheme> { error("Not initialized") }
private val LocalAppTypography = staticCompositionLocalOf<AppTypography> { error("Not initialized") }

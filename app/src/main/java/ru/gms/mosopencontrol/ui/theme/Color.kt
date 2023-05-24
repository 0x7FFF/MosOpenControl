package ru.gms.mosopencontrol.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

val LightColorScheme = AppColorScheme(
    primary = Color(0xFFF64027),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF212529),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFFF7F7F7),
    onTertiary = Color(0xFF212529),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF212529),
    surfaceVariant = Color(0x3C212529), // 30%
    outlineVariant = Color(0x64212529), // 50%
)


@Stable
class AppColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val background: Color,
    val onBackground: Color,
    val surfaceVariant: Color,
    val outlineVariant: Color,
)

package ru.gms.mosopencontrol.ui.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme
import ru.gms.mosopencontrol.ui.utils.toDisabledContainerColor
import ru.gms.mosopencontrol.ui.utils.toDisabledContentColor

object ButtonColors {

    @Composable
    fun primaryColorScheme(): ButtonColorsScheme =
        with(MosOpenControlTheme.colorScheme) {
            ButtonColorsScheme(
                containerColor = primary,
                contentColor = onPrimary,
                disabledContainerColor = primary.toDisabledContainerColor(),
                disabledContentColor = onPrimary.toDisabledContentColor(),
            )
        }


    @Composable
    fun tertiaryColorScheme(): ButtonColorsScheme =
        with(MosOpenControlTheme.colorScheme) {
            ButtonColorsScheme(
                containerColor = tertiary,
                contentColor = onTertiary,
                disabledContainerColor = tertiary.toDisabledContainerColor(),
                disabledContentColor = onTertiary.toDisabledContentColor(),
            )
        }

    @Composable
    fun primaryTransparentColorScheme(): ButtonColorsScheme =
        with(MosOpenControlTheme.colorScheme) {
            ButtonColorsScheme(
                containerColor = Color.Transparent,
                contentColor = primary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = primary.toDisabledContentColor(),
                isTransparent = true,
            )
        }
}

data class ButtonColorsScheme(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val isTransparent: Boolean = false,
    val isOutlined: Boolean = false,
)

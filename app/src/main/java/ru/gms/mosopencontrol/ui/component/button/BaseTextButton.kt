package ru.gms.mosopencontrol.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.DefaultRippleTheme
import ru.gms.mosopencontrol.ui.theme.TransparentRippleTheme
import ru.gms.mosopencontrol.ui.utils.toPressedContentColor

@Composable
internal fun BaseTextButton(
    text: String,
    textStyle: TextStyle,
    colorScheme: ButtonColorsScheme,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressedContentColor = remember { mutableStateOf(colorScheme.contentColor.toPressedContentColor()) }

    val rippleTheme =
        if (colorScheme.isTransparent) {
            TransparentRippleTheme
        } else {
            DefaultRippleTheme
        }

    val contentColor =
        if (isPressed) {
            pressedContentColor.value
        } else {
            colorScheme.contentColor
        }

    val colors =
        if (colorScheme.isOutlined) {
            outlinedButtonColors(
                containerColor = colorScheme.containerColor,
                contentColor = contentColor,
                disabledContainerColor = colorScheme.disabledContainerColor,
                disabledContentColor = colorScheme.disabledContentColor,
            )
        } else {
            ButtonDefaults.textButtonColors(
                containerColor = colorScheme.containerColor,
                contentColor = contentColor,
                disabledContainerColor = colorScheme.disabledContainerColor,
                disabledContentColor = colorScheme.disabledContentColor,
            )
        }

    val border =
        if (colorScheme.isOutlined) {
            BorderStroke(
                width = 1.0.dp,
                color = if (enabled) contentColor else colorScheme.disabledContentColor,
            )
        } else {
            null
        }
    CompositionLocalProvider(
        LocalRippleTheme provides rippleTheme
    ) {
        TextButton(
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = colors,
            shape = RoundedCornerShape(12.dp),
            border = border,
            contentPadding = contentPadding,
            onClick = onClick
        ) {
            Text(
                state = TextViewState(
                    text = text,
                    style = textStyle,
                    numberOfLines = 1,
                )
            )
        }
    }
}

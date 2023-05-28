package ru.gms.mosopencontrol.ui.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

@Composable
fun LargeButton(
    state: TextButtonViewState,
    modifier: Modifier = Modifier,
    colorScheme: ButtonColorsScheme = ButtonColors.primaryColorScheme(),
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BaseTextButton(
        text = state.text,
        textStyle = MosOpenControlTheme.typography.bodyLarge,
        colorScheme = colorScheme,
        contentPadding = PaddingValues(),
        modifier = modifier.height(48.dp),
        enabled = enabled,
        onClick = onClick,
    )
}

// region Preview

@Preview
@Composable
private fun ComposablePreviewPrimaryLargeButton() {
    MosOpenControlTheme {
        LargeButton(
            state = TextButtonViewState(text = "Go to pick up location"),
            onClick = { /*  Nothing to do. */ }
        )
    }
}

// endregion

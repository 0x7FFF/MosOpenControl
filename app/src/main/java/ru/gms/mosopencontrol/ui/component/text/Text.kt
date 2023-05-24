package ru.gms.mosopencontrol.ui.component.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun Text(
    state: TextViewState,
    modifier: Modifier = Modifier,
) {
    with(state) {
        if (this.text is AnnotatedString) {
            Text(
                modifier = modifier,
                text = text,
                style = style,
                color = color ?: Color.Unspecified,
                textAlign = alignment,
                maxLines = numberOfLines,
                overflow = TextOverflow.Ellipsis,
                textDecoration = decoration,
            )
        } else {
            Text(
                modifier = modifier,
                text = text.toString(),
                style = style,
                color = color ?: Color.Unspecified,
                textAlign = alignment,
                maxLines = numberOfLines,
                overflow = TextOverflow.Ellipsis,
                textDecoration = decoration,
            )
        }
    }
}

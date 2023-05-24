package ru.gms.mosopencontrol.ui.component.text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

open class TextViewState(
    val style: TextStyle,
    val text: CharSequence,
    val color: Color? = null,
    val alignment: TextAlign = TextAlign.Start,
    val numberOfLines: Int = Int.MAX_VALUE,
    val decoration: TextDecoration = TextDecoration.None,
)

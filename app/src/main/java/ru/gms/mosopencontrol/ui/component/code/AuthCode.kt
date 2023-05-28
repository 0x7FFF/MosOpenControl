package ru.gms.mosopencontrol.ui.component.code

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

private const val CODE_LENGTH = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthCode(
    isError: Boolean,
    modifier: Modifier = Modifier,
    onCodeChanged: (code: String, isFullyEntered: Boolean) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var textState by remember { mutableStateOf("") }
    var codes by remember { mutableStateOf(List(5) { "" }) }
    var current by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .background(Color.White)
            .onFocusChanged {
                if (it.isFocused) {
                    focusRequester.requestFocus()
                }
            }
    ) {
        TextField(
            value = textState,
            onValueChange = { value ->
                if (value.length <= CODE_LENGTH) {
                    for (index in 0 until CODE_LENGTH) {
                        codes = codes.toMutableList().also {
                            it[index] = (value.getOrNull(index) ?: "").toString()
                        }
                    }
                    textState = value
                    current = value.length
                    onCodeChanged(value, value.length == CODE_LENGTH)
                }
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        try {
                            focusRequester.requestFocus()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                cursorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        val isLastSymbol = current == CODE_LENGTH
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            codes.forEachIndexed { index, code ->
                Text(
                    modifier = Modifier
                        .clickable { focusRequester.requestFocus() }
                        .size(width = 42.dp, height = 48.dp)
                        .border(
                            width = 1.dp,
                            color = when {
                                isError && isLastSymbol ->
                                    MosOpenControlTheme.colorScheme.primary
                                current == index ->
                                    MosOpenControlTheme.colorScheme.onTertiary
                                else ->
                                    MosOpenControlTheme.colorScheme.outline
                            },
                            shape = RoundedCornerShape(12.dp),
                        )
                        .wrapContentSize(),
                    state = TextViewState(
                        text = code,
                        color = if (isError && isLastSymbol) {
                            MosOpenControlTheme.colorScheme.primary
                        } else {
                            MosOpenControlTheme.colorScheme.secondary
                        },
                        style = MosOpenControlTheme.typography.bodyLarge,
                        alignment = TextAlign.Center,
                    ),
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

// region Preview
@Preview
@Composable
private fun AuthCodePreview() {
    MosOpenControlTheme {
        AuthCode(
            isError = false,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            onCodeChanged = { _, _ -> }
        )
    }
}

// endregion

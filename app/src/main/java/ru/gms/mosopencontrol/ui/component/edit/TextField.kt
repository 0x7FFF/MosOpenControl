/*
 * ToYou
 * Copyright © 2023 Aram Meem Company Limited. All Rights Reserved.
 */

package ru.gms.mosopencontrol.ui.component.edit

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme

@Composable
fun TextField(
    text: String?,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    isError: Boolean = false,
    errorSupportingText: String? = null,
    isEnabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
) {
    var textState by remember(text) { mutableStateOf(text.orEmpty()) }
    var focusState by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textState,
        enabled = isEnabled,
        onValueChange = {
            textState = it
            onTextChange(it)
        },
        modifier = Modifier
            .onFocusChanged { focusState = it.isFocused }
            .then(modifier),
        textStyle = MosOpenControlTheme.typography.bodyLarge,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        label = {
            if (!hint.isNullOrEmpty()) {
                Text(
                    color = MosOpenControlTheme.colorScheme.outlineVariant,
                    text = if (focusState) "" else hint,
                    style = if (focusState) {
                        MosOpenControlTheme.typography.bodySmall
                    } else {
                        MosOpenControlTheme.typography.bodyLarge
                    },
                )
            }
        },
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        maxLines = maxLines,
        isError = isError,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MosOpenControlTheme.colorScheme.onBackground,
            unfocusedTextColor = MosOpenControlTheme.colorScheme.onBackground,
            disabledTextColor = MosOpenControlTheme.colorScheme.outline,
            focusedContainerColor =  Color.Transparent,
            unfocusedContainerColor =  Color.Transparent,
            disabledContainerColor =  Color.Transparent,
            cursorColor = MosOpenControlTheme.colorScheme.onBackground,
            errorCursorColor = MaterialTheme.colorScheme.error,
            focusedBorderColor = MosOpenControlTheme.colorScheme.outline,
            unfocusedBorderColor = MosOpenControlTheme.colorScheme.outline,
            disabledBorderColor = MosOpenControlTheme.colorScheme.outline,
            focusedLabelColor = MosOpenControlTheme.colorScheme.outline,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MosOpenControlTheme.colorScheme.outline,
        ),
        supportingText = null,
    )
}

// region Preview

@Preview
@Composable
fun ComposableCustomTextFieldPreviewInactive() {
    MosOpenControlTheme {
        TextField(
            text = "",
            hint = "Custom text",
            onTextChange = { }
        )
    }
}

// endregion


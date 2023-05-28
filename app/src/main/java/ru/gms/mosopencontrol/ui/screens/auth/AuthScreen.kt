package ru.gms.mosopencontrol.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.ui.component.button.ButtonColors
import ru.gms.mosopencontrol.ui.component.button.LargeButton
import ru.gms.mosopencontrol.ui.component.button.TextButtonViewState
import ru.gms.mosopencontrol.ui.component.edit.PhoneNumberVisualTransformation
import ru.gms.mosopencontrol.ui.component.edit.TextField
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme
import java.util.*

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        viewModel.navigateTo.collect { destination ->
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                navController.navigate(destination)
            }
        }
    }

    val state by viewModel.state.collectAsState()
    AuthContent(state, viewModel)

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AuthContent(
    state: AuthState,
    callback: AuthUiCallback,
) {
    val context = LocalContext.current
    val countryCode = remember {
        Locale.getDefault().country
    }
    val phoneNumberFormatter = remember {
        PhoneNumberUtil.createInstance(context)
    }

    var phoneNumber by remember { mutableStateOf(state.phoneNumber.orEmpty()) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 15.dp
                )
            ) {
                Spacer(modifier = Modifier.height(200.dp))
                Text(
                    state = TextViewState(
                        style = MosOpenControlTheme.typography.headlineMedium,
                        text = stringResource(id = R.string.auth_title),
                        color = MosOpenControlTheme.colorScheme.secondary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                )
                Text(
                    state = TextViewState(
                        style = MosOpenControlTheme.typography.bodyMedium,
                        text = stringResource(id = R.string.auth_subtitle),
                        color = MosOpenControlTheme.colorScheme.surfaceVariant,
                        alignment = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                )
                Spacer(modifier = Modifier.height(36.dp))
                TextField(
                    text = phoneNumber,
                    onTextChange = {
                        phoneNumber = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                keyboardController?.show()
                            }
                        },
                    singleLine = true,
                    hint = stringResource(id = R.string.auth_phone_hint),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    visualTransformation = remember {
                        PhoneNumberVisualTransformation(
                            phoneNumberFormatter.getAsYouTypeFormatter(countryCode)
                        )
                    }
                )
                Spacer(modifier = Modifier.weight(1.0f))
                LargeButton(
                    state = TextButtonViewState(
                        text = stringResource(id = R.string.auth_action_enter),
                    ),
                    enabled = isValidNumber(phoneNumber, countryCode, phoneNumberFormatter),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    keyboardController?.hide()
                    callback.onAuthClick(phoneNumber)
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            if (state.inProgress) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MosOpenControlTheme.colorScheme.background.copy(alpha = 0.8f)
                    )
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(),
                        color = MosOpenControlTheme.colorScheme.primary,
                    )
                }
            }

            state.error?.let {
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
            }
        }
    }
}

private fun isValidNumber(
    number: String,
    countryCode: String,
    phoneNumberUtil: PhoneNumberUtil,
): Boolean =
    try {
        phoneNumberUtil.isValidNumber(
            phoneNumberUtil.parse(number, countryCode)
        )
    } catch (e: NumberParseException) {
        false
    }


// region Preview
@Preview
@Composable
private fun AuthContentPreview() {
    MosOpenControlTheme {
        AuthContent(
            AuthState(
                phoneNumber = null,
                inProgress = true,
                error = null,
            ),
            object : AuthUiCallback {
                override fun onAuthClick(phoneNumber: String) {
                    // Nothing to do.
                }

                override fun onReqClick() {
                    // Nothing to do.
                }
            },
        )
    }
}
// endregion

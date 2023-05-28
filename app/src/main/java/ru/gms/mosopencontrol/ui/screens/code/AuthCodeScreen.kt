package ru.gms.mosopencontrol.ui.screens.code

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.ui.component.button.ButtonColors
import ru.gms.mosopencontrol.ui.component.button.LargeButton
import ru.gms.mosopencontrol.ui.component.button.TextButtonViewState
import ru.gms.mosopencontrol.ui.component.edit.TextField
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme


@Composable
fun AuthCodeScreen(
    viewModel: AuthCodeViewModel,
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        viewModel.navigateTo.collect { destination ->
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (destination == "back") {
                    navController.popBackStack()
                } else {
                    navController.navigate(destination)
                }
            }
        }
    }

    val state by viewModel.state.collectAsState()
    AuthCodeContent(state, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthCodeContent(
    state: AuthCodeState,
    callback: AuthCodeUiCallback,
) {
    val codeState = remember { mutableStateOf(Array(5) { "" }) }
    val errorState = remember { mutableStateOf(Array(5) { false }) }
    val focusRequesters = remember { List(5) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val codes = remember { mutableStateOf(Array(5) { "" }) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = callback::onBackClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MosOpenControlTheme.colorScheme.background,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 18.dp),
                ) {
                    Text(
                        state = TextViewState(
                            style = MosOpenControlTheme.typography.headlineMedium,
                            text = stringResource(id = R.string.auth_code_title),
                            color = MosOpenControlTheme.colorScheme.secondary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.height(9.dp))
                    Text(
                        state = TextViewState(
                            style = MosOpenControlTheme.typography.bodyMedium,
                            text = stringResource(id = R.string.auth_code_subtitle),
                            color = MosOpenControlTheme.colorScheme.surfaceVariant,
                            alignment = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(),
                    ) {
                        for (i in 0..4) {
                            TextField(
                                text = codeState.value[i],
                                onTextChange = { newValue ->
                                    codes.value[i] = newValue
                                    if (newValue.length == 1) {
                                        if (i < 4) {
                                            focusRequesters[i + 1].requestFocus()
                                        } else {
                                            callback.onCodeEntered(codes.value.joinToString(""))
                                        }
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(45.dp)
                                    .onFocusChanged { focusState ->
                                        if (focusState.isFocused) {
                                            keyboardController?.show()
                                        }
                                    }
                                    .focusRequester(focusRequesters[i])
                                    .onKeyEvent { event ->
                                        if (event.key == Key.Backspace &&
                                            event.type == KeyEventType.KeyUp &&
                                            codes.value[i].isEmpty() && i > 0
                                        ) {
                                            focusRequesters[i - 1].requestFocus()
                                            true
                                        } else {
                                            false
                                        }
                                    },
                                isError = errorState.value[i],
                            )
                        }
                    }

                    if (state.isSuccessVerify == false) {
                        Text(
                            state = TextViewState(
                                style = MosOpenControlTheme.typography.bodyMedium,
                                text = stringResource(id = R.string.auth_code_incorrect),
                                color = MosOpenControlTheme.colorScheme.primary,
                                alignment = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize()
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    LargeButton(
                        state = TextButtonViewState(
                            text = stringResource(id = R.string.auth_code_request)
                                + if (state.timer != 0) " 0:${state.timer.toString()}" else ""
                        ),
                        colorScheme = ButtonColors.tertiaryColorScheme(),
                        enabled = state.timer == 0,
                        onClick = callback::onReqClick,
                        modifier = Modifier.fillMaxWidth()
                    )
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
    )
}

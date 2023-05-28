package ru.gms.mosopencontrol.ui.screens.code

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch
import ru.gms.mosopencontrol.R
import ru.gms.mosopencontrol.ui.component.button.ButtonColors
import ru.gms.mosopencontrol.ui.component.button.LargeButton
import ru.gms.mosopencontrol.ui.component.button.TextButtonViewState
import ru.gms.mosopencontrol.ui.component.code.AuthCode
import ru.gms.mosopencontrol.ui.component.text.Text
import ru.gms.mosopencontrol.ui.component.text.TextViewState
import ru.gms.mosopencontrol.ui.theme.MosOpenControlTheme
import java.util.*


@Composable
fun AuthCodeScreen(
    phoneNumber: String,
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
                    navController.popBackStack(route = "auth", inclusive = true)
                    navController.navigate(destination)
                }
            }
        }
    }

    val context = LocalContext.current
    val countryCode = remember {
        Locale.getDefault().country
    }
    val phoneNumberFormatter = remember {
        PhoneNumberUtil.createInstance(context)
    }

    val state by viewModel.state.collectAsState()
    AuthCodeContent(
        phoneNumberFormatter.format(
            phoneNumberFormatter.parse(phoneNumber, countryCode),
            PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL,
        ),
        state,
        viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthCodeContent(
    phoneNumber: String,
    state: AuthCodeState,
    callback: AuthCodeUiCallback,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    var isCodeEntered by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MosOpenControlTheme.colorScheme.background,
                ),
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = callback::onBackClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = MosOpenControlTheme.colorScheme.primary,
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
                            text = buildAnnotatedString {
                                append(stringResource(id = R.string.auth_code_subtitle))
                                append(" ")
                                withStyle(
                                    style = SpanStyle(color =MosOpenControlTheme.colorScheme.secondary)
                                ) {
                                    append(phoneNumber)
                                }
                            },
                            color = MosOpenControlTheme.colorScheme.surfaceVariant,
                            alignment = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    AuthCode(
                        isError = state.isSuccessVerify == false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(),
                        onCodeChanged = { code, isFullyEntered ->
                            isCodeEntered = isFullyEntered
                            if (isFullyEntered) {
                                callback.onCodeEntered(code)
                            }
                        },
                    )

                    if (state.isSuccessVerify == false && isCodeEntered) {
                        keyboardController?.show()
                        Spacer(modifier = Modifier.height(24.dp))
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
                                + if (state.timer != 0) " 0:${String.format("%02d", state.timer)}" else ""
                        ),
                        colorScheme = ButtonColors.tertiaryColorScheme(),
                        enabled = state.timer == 0,
                        onClick = callback::onReqClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (state.inProgress) {
                    keyboardController?.hide()
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
                    keyboardController?.show()
                    scope.launch {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }
        }
    )
}

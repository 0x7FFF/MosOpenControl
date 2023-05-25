package ru.gms.mosopencontrol.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gms.mosopencontrol.model.usecases.AuthUseCase
import ru.gms.mosopencontrol.ui.screens.auth.Action.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel(),
    AuthUiCallback {

    private val _state = MutableStateFlow(
        AuthState(
            phoneNumber = null,
            inProgress = false,
            error = null,
        )
    )
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val reduce: (AuthState, Action) -> AuthState = { state, action ->
        when(action) {
            is Auth -> {
                state.copy(
                    phoneNumber = action.phoneNumber,
                    inProgress = true,
                    error = null,
                )
            }
            is Error -> {
                state.copy(
                    inProgress = false,
                    error = action.error.message
                )
            }
            is SuccessAuth -> {
                state.copy(
                    inProgress = false,
                    error = null,
                )
            }
        }
    }

    override fun onAuthClick(phoneNumber: String) {
        dispatch(Auth(phoneNumber))
        viewModelScope.launch {
            try {
                authUseCase(phoneNumber)
                dispatch(SuccessAuth)
            } catch (e: Exception) {
                dispatch(Error(e))
            }
        }
    }

    override fun onReqClick() {
        TODO("Not yet implemented")
    }

    private fun dispatch(action: Action) {
        _state.update { state -> reduce(state, action) }
    }
}

interface AuthUiCallback {
    fun onAuthClick(phoneNumber: String)

    fun onReqClick()
}

private sealed interface Action {
    data class Auth(val phoneNumber: String) : Action

    data class Error(val error: Throwable) : Action

    object SuccessAuth: Action
}

data class AuthState(
    val phoneNumber: String?,
    val inProgress: Boolean,
    val error: String?,
)

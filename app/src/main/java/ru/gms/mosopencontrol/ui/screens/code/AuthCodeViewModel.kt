package ru.gms.mosopencontrol.ui.screens.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gms.mosopencontrol.model.usecases.VerifyCodeUseCase
import ru.gms.mosopencontrol.ui.screens.code.Action.Error
import ru.gms.mosopencontrol.ui.screens.code.Action.UpdateTimer
import ru.gms.mosopencontrol.ui.screens.code.Action.VerifyCode
import ru.gms.mosopencontrol.ui.screens.code.Action.VerifyCodeResult
import ru.gms.mosopencontrol.ui.utils.ResourceManager
import ru.gms.mosopencontrol.ui.utils.formatError
import javax.inject.Inject

@HiltViewModel
class AuthCodeViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val resourceManager: ResourceManager,
) : ViewModel(),
    AuthCodeUiCallback {

    private val _state = MutableStateFlow(
        AuthCodeState(
            timer = TIMER,
            inProgress = false,
            error = null,
            isSuccessVerify = null,
        )
    )
    val state: StateFlow<AuthCodeState> = _state.asStateFlow()

    private val _navigateTo = MutableSharedFlow<String>()
    val navigateTo = _navigateTo.asSharedFlow()

    private var countdownJob: Job? = null

    private val reduce: (AuthCodeState, Action) -> AuthCodeState = { state, action ->
        when (action) {
            is Error -> {
                state.copy(
                    inProgress = false,
                    error = action.error.formatError(resourceManager)
                )
            }
            is UpdateTimer -> {
                state.copy(
                    timer = action.timer,
                    error = null,
                )
            }
            is VerifyCode -> {
                state.copy(
                    inProgress = true,
                    error = null,
                )
            }
            is VerifyCodeResult -> {
                state.copy(
                    inProgress = false,
                    isSuccessVerify = action.isSuccess,
                    error = null,
                )
            }
        }
    }

    init {
        onReqClick()
    }

    override fun onCodeEntered(code: String) {
        dispatch(VerifyCode(code))
        viewModelScope.launch {
            try {
                val isSuccess = verifyCodeUseCase(code)
                dispatch(VerifyCodeResult(isSuccess))
                if (isSuccess) {
                    _navigateTo.emit("main")
                }
            } catch (e: Throwable) {
                dispatch(Error(e))
            }
        }
    }

    override fun onReqClick() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in TIMER downTo 0) {
                dispatch(UpdateTimer(i))
                delay(1000)
            }
        }
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _navigateTo.tryEmit("back")
        }
    }

    private fun dispatch(action: Action) {
        _state.update { state -> reduce(state, action) }
    }

    private companion object {
        const val TIMER = 30
    }
}

interface AuthCodeUiCallback {
    fun onCodeEntered(code: String)

    fun onReqClick()
    fun onBackClick()
}

private sealed interface Action {
    data class UpdateTimer(val timer: Int) : Action

    data class VerifyCode(val code: String) : Action

    data class Error(val error: Throwable) : Action

    data class VerifyCodeResult(val isSuccess: Boolean) : Action
}

data class AuthCodeState(
    val timer: Int,
    val inProgress: Boolean,
    val error: String?,
    val isSuccessVerify: Boolean?
)

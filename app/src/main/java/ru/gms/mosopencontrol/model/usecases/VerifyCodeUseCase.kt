package ru.gms.mosopencontrol.model.usecases

import ru.gms.mosopencontrol.model.repo.AuthRepository
import javax.inject.Inject

class VerifyCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(code: String): Boolean =
        authRepository.verifyAuthCode(code)
}

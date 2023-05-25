package ru.gms.mosopencontrol.model.usecases

import ru.gms.mosopencontrol.model.repo.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(phoneNumber: String) {
        authRepository.requestAuthCode(phoneNumber)
    }
}

package ru.gms.mosopencontrol.model.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    suspend fun requestAuthCode(phoneNumber: String) {
        withContext(Dispatchers.IO) {
            delay(1000)
            if (phoneNumber == "+79111111111") {
                throw IOException()
            } else if (phoneNumber == "+79111111112") {
                throw RuntimeException()
            }
        }
    }

    suspend fun verifyAuthCode(code: String): Boolean {
        withContext(Dispatchers.IO) {
            delay(1000)
        }
        return when (code) {
            "11111" -> {
                throw IOException()
            }
            "22222" -> {
                false
            }
            else -> {
                true
            }
        }
    }
}

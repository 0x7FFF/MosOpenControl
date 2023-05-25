package ru.gms.mosopencontrol.model.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    suspend fun requestAuthCode(phoneNumber: String) {
        withContext(Dispatchers.IO) {
            delay(1000)
        }
    }
}

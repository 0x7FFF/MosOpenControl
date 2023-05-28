package ru.gms.mosopencontrol.ui.utils

import ru.gms.mosopencontrol.R
import java.io.IOException

fun Throwable.formatError(resourceManager: ResourceManager): String =
    when (this) {
        is IOException ->
            resourceManager.getString(R.string.error_no_internet)
        else ->
            resourceManager.getString(R.string.error_server_unknown)
    }

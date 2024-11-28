package com.example.mapkit.core.util.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun ticker(
    delayMillis: Long
) = flow {
    while (true) {
        delay(delayMillis)
        emit(Unit)
    }
}
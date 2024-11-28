package com.example.mapkit.domain.util

@DslMarker
annotation class ResourceDSL

@ResourceDSL
sealed class ResourceUI<out T> {
    data class Resource<T>(val data: T, val statusCode: Int = 200) : ResourceUI<T>()
    data class Error(val error: Throwable, val statusCode: Int? = 0) : ResourceUI<Nothing>()
}
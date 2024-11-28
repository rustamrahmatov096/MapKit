package com.example.mapkit.data.models

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class BaseCustomResponse<T>(
    @SerialName("success") var success: Boolean,
    @SerialName("result") var result: BaseCustomData<T>,
)
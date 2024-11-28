package com.example.mapkit.data.models

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class BaseData2<T>(
    @SerialName("data") var data: T?,
)
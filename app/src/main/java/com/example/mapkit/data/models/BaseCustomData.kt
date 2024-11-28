package com.example.mapkit.data.models

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
data class BaseCustomData<T>(
    @SerialName("code") var code: Int,
    @SerialName("message") var message: String?,
    @SerialName("audit") var audit: String?,
    @SerialName("data") var data: BaseData2<T>,
)
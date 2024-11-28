package com.example.mapkit.data.unit

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName



@Keep
data class RefreshBodyData(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("result") var result: RefreshBodyResult? = null,
)

@Keep
data class RefreshBodyResult(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("audit") var audit: String? = null,
    @SerializedName("data") var data: Object? = null,
)
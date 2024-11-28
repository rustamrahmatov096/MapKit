package com.example.mapkit.data.models.api.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TokenConfirmResponse(

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("permissions")
    val permissions: List<String?>? = null,

    @field:SerializedName("roleName")
    val roleName: String? = null,

    @field:SerializedName("refreshTokenExpire")
    val refreshTokenExpire: String? = null,

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("accessTokenExpire")
    val accessTokenExpire: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null
)
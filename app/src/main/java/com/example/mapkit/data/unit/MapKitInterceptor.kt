package com.example.mapkit.data.unit

import com.example.mapkit.data.local.AppCache
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MapKitInterceptor @Inject constructor(
    private val appCache: AppCache,
    private val idProvider: IdProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val appLang = when (appCache.language) {
            "uz" -> "UZB"
            "ru" -> "RUS"
            "en" -> "ENG"
            "kaa" -> "KAA"
            "uz-rUZ" -> "KRL"
            else -> "UZB"
        }

        val requestBuilder: Request.Builder = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("X-App-Version", idProvider.getVersionName())
            .header("X-App-Build", idProvider.getVersionCode().toString())
            .header("X-Device-Type", idProvider.deviceType())
            .header("X-Device-Model", idProvider.getDeviceName())
            .header("X-Device-ID", idProvider.getDeviceID())
            .header("X-OS", idProvider.getDeviceNameAndOSVersion())
            .header("X-Lang", appLang)
            .header("X-Fcm-Token", appCache.FCMToken)
//            .header("timestamp", idProvider.timestamp().toString())
//            .header("X-Device-Uid", idProvider.getAppId())

        if (idProvider.getDeviceMode() != "") {
            requestBuilder.header("X-Dev-Mode", idProvider.getDeviceMode().uppercase())
        }

        if (appCache.accessToken.isNullOrBlank().not()) {
            requestBuilder.header("X-Auth-Token", "${appCache.accessToken}")
        }

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
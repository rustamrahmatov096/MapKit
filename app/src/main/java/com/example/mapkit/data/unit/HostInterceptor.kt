package com.example.mapkit.data.unit

import com.example.mapkit.BuildConfig
import com.example.mapkit.data.local.AppCache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HostXaznaInterceptor @Inject constructor(
    private val appCache: AppCache
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseUrl: String = BuildConfig.BASE_URL
//        if (appCache.isTestServer) BuildConfig.BASE_URL_TEST else BuildConfig.BASE_URL_PROD
        val host: HttpUrl? = baseUrl.toHttpUrlOrNull()

        val newRequest = host?.let {
            val newUrl = chain.request().url.newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()

            return@let chain.request().newBuilder()
                .url(newUrl)
                .build()

        } ?: chain.request()

        return chain.proceed(newRequest)
    }
}


@Singleton
class HostFileInterceptor @Inject constructor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseUrl: String = "BuildConfig.BASE_URL_FILE"
        val host: HttpUrl? = baseUrl.toHttpUrlOrNull()

        val newRequest = host?.let {
            val newUrl = chain.request().url.newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()

            return@let chain.request().newBuilder()
                .url(newUrl)
                .build()

        } ?: chain.request()

        return chain.proceed(newRequest)
    }
}
package com.example.mapkit.data.unit

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mapkit.data.local.AppCache
import com.example.mapkit.data.models.BaseResponse
import com.example.mapkit.data.models.api.response.TokenConfirmResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRefreshTokenInterceptor @Inject constructor(
    private val appCache: AppCache,
    @ApplicationContext val context: Context,
    private val mapKitInterceptor: MapKitInterceptor,
) : Interceptor {

    private var passedTime: Long = 0

    override fun intercept(chain: Interceptor.Chain): Response {

        val token: String? = appCache.accessToken

        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        var refreshBodyData: RefreshBodyData? = null
        val responseBodyCopy = response.peekBody(Long.MAX_VALUE)

        try {
            val gson = Gson()
            refreshBodyData = gson.fromJson(responseBodyCopy.string(), RefreshBodyData::class.java)
        } catch (e: Exception) {
            Log.d("AUTH_REFRESH_ERROR", e.localizedMessage)
        }

        return if (refreshBodyData?.result?.code == 30402) {

            Log.d("AUTH_REFRESH", "REFRESH 20402")
            synchronized(this) {
                val currentToken: String? = appCache.accessToken

                if (currentToken != null && (token == currentToken) && (Date().time - passedTime) > (1000 * 10)) {


//                    val interceptor = HttpLoggingInterceptor()
//                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)



                    val client: OkHttpClient = OkHttpClient.Builder()
                        .addInterceptor(ChuckerInterceptor(context))
                        .addInterceptor(mapKitInterceptor)
//                        .certificatePinner(certificatePinner)
//                        .addInterceptor(interceptor)
                        .build()

                    val json = JsonObject()
                    json.addProperty("accessToken", appCache.accessToken)
                    json.addProperty("refreshToken", appCache.refreshToken)

                    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                    val body = json.toString().toRequestBody(mediaType)
//
                    val requestToken = Request.Builder()
                        .url("https://url" + "auth/v2/login/refresh-token")
                        .post(body)
                        .build()


                    val responseClient = client.newCall(requestToken).execute()

                    val type = object : TypeToken<BaseResponse<TokenConfirmResponse>?>() {}.type

                    val responseString = responseClient.body.string()


                    val loginResponse = Gson().fromJson<BaseResponse<TokenConfirmResponse>>(
                        responseString,
                        type
                    )


                    if (loginResponse.success) {

                        appCache.accessToken = loginResponse.result.data?.accessToken
                        appCache.refreshToken = loginResponse.result.data?.refreshToken
                        passedTime = Date().time
                    } else {

                        passedTime = Date().time

                        return@synchronized response.newBuilder()
                            .code(responseClient.code)
                            .body(responseString.toResponseBody())
                            .message(responseClient.message)
                            .build()

                    }

                    val freshAccessToken: String = appCache.accessToken.toString()

                    val newRequest =
                        request.newBuilder()
                            .header("X-Auth-Token", freshAccessToken)
                            .build()
                    return chain.proceed(newRequest)

                } else {

                    val freshAccessToken: String = appCache.accessToken.toString()

                    val newRequest =
                        request.newBuilder()
                            .header("X-Auth-Token", freshAccessToken)
                            .build()

                    return chain.proceed(newRequest)
                }
            }
        } else {
            response
        }
    }
}
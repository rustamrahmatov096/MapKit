package com.example.mapkit.data.unit

import com.example.mapkit.data.models.BaseResponse
import com.example.mapkit.domain.util.ResourceUI
import com.example.mapkit.domain.util.ResourceUI.Error
import kotlinx.coroutines.delay
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@DslMarker
annotation class WrapperDsl

class ResponseWrapper @Inject constructor() {


    private val DEFAULT_RETRY_COUNT = 3
    private val DEFAULT_RETRY_DELAY_MILLIS = 1000L

    suspend fun <T, E> wrapperX(
        mapper: Mapper<T, E>,
        body: suspend () -> Response<BaseResponse<E>>,
        retryCount: Int = DEFAULT_RETRY_COUNT,
        retryDelayMillis: Long = DEFAULT_RETRY_DELAY_MILLIS
    ): ResourceUI<T> {
        var currentRetry = 0
        var response: Response<BaseResponse<E>>? = null

        while (currentRetry < retryCount) {
            try {
                response = body()
                if (response.isSuccessful) {
                    return checkStatus(mapper, response)
                }
            } catch (e: Exception) {
                if (shouldRetry(e) && currentRetry < retryCount - 1) {
                    delay(retryDelayMillis)
                } else {
                    return handleError(e, response)
                }
            }
            currentRetry++
        }
        return Error(Exception("Failed after $retryCount retries"), response?.code())
    }

    private fun shouldRetry(e: Exception): Boolean {
        return when (e) {
            is SocketTimeoutException,
            is IOException,
            is UnknownHostException -> true
            else -> false
        }
    }

    private fun <T, E> handleError(exception: Exception, response: Response<BaseResponse<E>>?): ResourceUI<T> {
        return when (exception) {
            is UnknownHostException -> Error(Exception("Not connected to the server"))
            is SocketTimeoutException -> Error(Exception("Connection timed out"))
            is IOException -> Error(Exception("IOException occurred"))
            else -> Error(exception, response?.code())
        }
    }

    suspend fun <T, E> wrapperX(
        mapper: Mapper<T, E>,
        body: suspend () -> Response<BaseResponse<E>>
    ): ResourceUI<T> {
        return try {
            val response: Response<BaseResponse<E>> = body()
            checkStatus(mapper, response)
        } catch (e: Exception) {
//            Timber.d("eee: wrapperX 1 $e")
            when (e) {
                is NetworkException -> Error(Exception("Data is Empty"), body().code())
                is TokenWrongException -> Error(
                    TokenWrongException("Token wrong EXP"),
                    body().code()
                )
//                is TokenWrongLogoutException -> Error(
//                    TokenWrongLogoutException("Token wrong EXP"),
//                    body().code()
//                )
                is Token20403Exception -> Error(
                    Token20403Exception("Token wrong 20403"),
                    body().code()
                )
//                is NullPointerException -> Error(Exception("Data is Empty"))
                is UnknownHostException -> Error(ServerException("Not connection with server"))
//                is SSLHandshakeException -> Error(ServerException("Not connection with server"))
//                is SSLException -> Error(ServerException("Not connection with server"))
//                is SocketException -> Error(SocketException("Not connection with server"))
                is SocketTimeoutException -> Error(Exception("Not connection with server"))
                is IOException -> Error(Exception(e))
//            else -> Error(Exception("Not connection with server."))
                else -> Error(e, body().body()?.result?.code)
            }
        }
    }

    private fun <T, E> checkStatus(
        mapper: Mapper<T, E>,
        response: Response<BaseResponse<E>>
    ): ResourceUI<T> {
        val body: BaseResponse<E>? = response.body()
        return if (response.body()?.success == true) {
            val data: T = body?.result!!.data.let { mapper.mapData(it!!) }
            ResourceUI.Resource(data, response.body()?.result?.code!!)
        } else {
            var message: String? = "Something went wrong"

            response.body().let {
                message = handleError(it?.result?.message)
            }

            if (response.code() == 999) {
                message = handleError("Not Internet Connection")
                return Error(NoConnectionException(message!!))
            }

            when (response.body()?.result?.code) {
                10401 -> {
                    Error(AppUpdateException("$message"))
                }

                10402 -> {
                    Error(Exception("Text pereriv"), response.code())
                }

                30401 -> {
                    Error(TokenWrongException("Token wrong EXP"), response.code())
                }
//                20402 -> {
//                    Error(TokenWrongLogoutException("Token wrong EXP"), response.code())
//                }
                20403 -> {
                    Error(Token20403Exception("$message 20403"), response.code())
                }

                20400 -> {
                    Error(Exception(message), response.code())
                }

                else -> {
                    if (response.body() != null)
                        Error(NetworkException(message!!), response.body()?.result?.code)
                    else
                        Error(NetworkException(message!!), response.code())
                }
            }
        }
    }

    suspend fun <T, E> wrapperX(
        mapper: (dto: E) -> T,
        body: suspend () -> Response<BaseResponse<E>>
    ): ResourceUI<T> {
//        Timber.d("eee: wrapperX 2 mapper $mapper ")
        return try {
            val response: Response<BaseResponse<E>> = body()
//            Timber.d("eee: wrapperX 2 response $response")
            checkStatus(mapper, response)
        } catch (e: Exception) {
//            Timber.d("eee: wrapperX 2 $e")
            when (e) {
                is NetworkException -> Error(Exception("Data is Empty"))
                is TokenWrongException -> Error(
                    TokenWrongException("Token wrong EXP"),
                    body().code()
                )
//                is TokenWrongLogoutException -> Error(
//                    TokenWrongLogoutException("Token wrong EXP"),
//                    body().code()
//                )
                is Token20403Exception -> Error(
                    Token20403Exception("Token wrong 20403"),
                    body().code()
                )
//                is NullPointerException -> Error(Exception("Data is Empty"))
                is UnknownHostException -> Error(ServerException("Not connection with server"))
//                is SSLHandshakeException -> Error(ServerException("Not connection with server"))
//                is SSLException -> Error(ServerException("Not connection with server"))
//                is SocketException -> Error(SocketException("Not connection with server"))
                is SocketTimeoutException -> Error(Exception("Not connection with server"))
//            else -> Error(Exception("Not connection with server."))
                else -> Error(e)
            }
        }
    }

    private fun <T, E> checkStatus(
        mapper: (dto: E) -> T,
        response: Response<BaseResponse<E>>
    ): ResourceUI<T> {
        val body: BaseResponse<E>? = response.body()
        // Timber.d("eee: wrapperX 2 body $body")
        return if (response.body()!!.success) {
            val data: T = body?.result!!.data.let { mapper.invoke(it!!) }
//            Timber.d("eee: wrapperX 2 data $data")
            ResourceUI.Resource(data, response.body()!!.result.code)
        } else {
            var message: String? = "Something went wrong"

            if (response.body() != null)
                message = handleError(response.body()!!.result.message)

            if (response.code() == 999) {
                message = handleError("Not Internet Connection")
            }

            when (response.body()?.result?.code) {
                10401 -> {
                    Error(AppUpdateException("$message"), response.code())
                }

                10402 -> {
                    Error(Exception("Text pereriv"), response.code())
                }

                30401 -> {
                    Error(TokenWrongException("Token wrong EXP"), response.code())
                }
//                20402 -> {
//                    Error(TokenWrongLogoutException("Token wrong EXP"), response.code())
//                }
                20403 -> {
                    Error(Token20403Exception("$message 20403"), response.code())
                }

                20400 -> {
                    Error(Exception(message), response.code())
                }

                else -> {
                    Error(NetworkException(message!!), response.body()?.result?.code)
                }
            }
        }
    }

    private fun handleError(body: String?): String? {
        return body
//        val tempError = """{ "errorMessage" = "Some Error from network" }"""
//        val byteArray: ByteArray = body?.bytes() ?: tempError.toByteArray()
//        val byteArray: ByteArray = body?.bytes() ?: tempError.toByteArray()
//        return try {
//            JSONObject(String(byteArray)).getString("errorMessage")
//        } catch (e: JSONException) {
////            "Some Error from network"
//            e.message.toString()
//        }
    }
}
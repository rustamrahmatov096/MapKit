package com.example.mapkit.app.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkit.core.util.vm.SingleLiveEvent
import com.example.mapkit.data.unit.AppUpdateException
import com.example.mapkit.data.unit.GlobalException
import com.example.mapkit.data.unit.InfoForUser
import com.example.mapkit.data.unit.NeedPassChangeException
import com.example.mapkit.data.unit.NetworkException
import com.example.mapkit.data.unit.NoConnectionException
import com.example.mapkit.data.unit.ServerException
import com.example.mapkit.data.unit.Token20403Exception
import com.example.mapkit.data.unit.TokenWrongException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseVM : ViewModel() {

    private val _globalErrorLiveData = SingleLiveEvent<String>()
    val globalErrorLiveData: LiveData<String> = _globalErrorLiveData

    private val _logoutScreenLiveData = SingleLiveEvent<Unit>()
    val logoutScreenLiveData: LiveData<Unit> = _logoutScreenLiveData

    private val _noConnection = SingleLiveEvent<Unit>()
    val noConnection: LiveData<Unit> get() = _noConnection

    private val _needUpdateScreenLiveData = SingleLiveEvent<String>()
    val needUpdateScreenLiveData: LiveData<String> = _needUpdateScreenLiveData

    protected fun launchVM(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(context, start, block)

    protected fun globalError(error: Throwable) {
        when (error) {
            is ServerException -> {
//                onNetworkException(getString(R.string.error_with_server))
            }

            is NetworkException -> {
                if (error.message?.contains("unexpected end of stream") == false
                    && error.message?.contains("http") == false
                    && error.message?.contains("Exception") == false
                    && error.message?.contains("token") == false
                    && error.message?.contains("токен") == false
                )
                    _globalErrorLiveData.value = error.message
            }

            is NoConnectionException -> {
                _noConnection.value = Unit
            }

            is GlobalException -> {
//                onGlobalException(
//                    error.message,
//                    (error as GlobalException).statusCode
//                )
            }

            is TokenWrongException -> {
                _logoutScreenLiveData.value = Unit
            }

            is Token20403Exception -> {

            }

//            is Token20400Exception -> {
//                _logoutScreenLiveData.value = Unit
//            }

            is NeedPassChangeException -> {
//                (activity as GlobalListener).changePassword()
            }

            is AppUpdateException -> {
                _needUpdateScreenLiveData.value = error.message
            }

            is InfoForUser -> {
//                infoDialog(error.message).show()
            }

            is SocketException -> {

            }

            is SocketTimeoutException -> {
                if (error.message?.contains("unexpected end of stream") == false
                    && error.message?.contains("http") == false
                    && error.message?.contains("Exception") == false
                    && error.message?.contains("token") == false
                    && error.message?.contains("токен") == false
                )
                    _globalErrorLiveData.value = error.message
            }

            is IOException -> {
//                Log.d("IOException", error.message.toString())
            }

            else -> {
                // TODO: unexpected of stream xatoligini hal qilish kerak, hozircha chiqarmayapmiz UI ga
                if (error.message?.contains("unexpected end of stream") == false
                    && error.message?.contains("http") == false
                    && error.message?.contains("Exception") == false
                    && error.message?.contains("token") == false
                    && error.message?.contains("токен") == false
                )
                    _globalErrorLiveData.value = error.message
            }
        }
    }
}
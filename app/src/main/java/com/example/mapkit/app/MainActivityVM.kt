package com.example.mapkit.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mapkit.core.util.vm.SingleLiveEvent
import com.example.mapkit.data.local.AppCache
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    val appCache: AppCache,
    application: Application
) : AndroidViewModel(application) {


    private val _myIdErrorLiveData = SingleLiveEvent<String>()
    val myIdErrorLiveData: LiveData<String> = _myIdErrorLiveData

    private var exitTime: Long = 0

    init {
//        getDocument()
    }

    fun getCurrentLang() = appCache.language

    fun showError(message: String) {
        _myIdErrorLiveData.value = message
    }

    fun saveFcmToken(token: String) {
        appCache.FCMToken = token
    }

    fun getCurrentTheme() = appCache.themeType


    fun setExitTime(time: Long) {
        exitTime = time
        appCache.exitTime = time.toString()
    }

    fun isShowPin(): Boolean {
        // start from splash screen
        return when {
            appCache.pinCode.isNullOrEmpty() -> false
            (Date().time - exitTime) >= (5000 * 60) -> true //  after 5 minutes
            else -> false
        }
    }


    suspend fun logout(): Boolean {
       return false
    }
}
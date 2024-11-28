package com.example.mapkit.app.ui.screen.splash

import androidx.lifecycle.LiveData
import com.example.mapkit.app.base.BaseVM
import com.example.mapkit.core.util.vm.SingleLiveEvent
import com.example.mapkit.data.local.AppCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenVM @Inject constructor(
    private val appCache: AppCache,
) : BaseVM() {

    private val _homeScreenLiveData = SingleLiveEvent<Unit>()
    val homeScreenLiveData: LiveData<Unit> = _homeScreenLiveData

    private val _loginScreenLiveData = SingleLiveEvent<Unit>()
    val loginScreenLiveData: LiveData<Unit> = _loginScreenLiveData

    fun nextScreen() {
        launchVM {
//            delay(500)
//            if (appCache.accessToken.isNullOrBlank() || appCache.pinCode.isNullOrBlank()) _loginScreenLiveData.value = Unit
//            else _homeScreenLiveData.value = Unit

            _homeScreenLiveData.value = Unit
        }
    }
}
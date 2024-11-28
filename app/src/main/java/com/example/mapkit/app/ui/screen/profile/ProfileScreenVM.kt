package com.example.mapkit.app.ui.screen.profile

import com.example.mapkit.app.base.BaseVM
import com.example.mapkit.data.local.AppCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileScreenVM @Inject constructor(
    val appCache: AppCache,

) : BaseVM() {


}
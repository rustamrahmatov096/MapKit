package com.example.mapkit.app.ui.screen.main

import androidx.lifecycle.SavedStateHandle
import com.example.mapkit.app.base.BaseVM
import com.example.mapkit.data.local.AppCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val appCache: AppCache
) : BaseVM() {



}
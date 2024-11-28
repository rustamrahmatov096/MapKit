package com.example.mapkit.app.ui.screen.yandex_map

import com.example.mapkit.app.base.BaseVM
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.data.local.AppCache
import com.example.mapkit.domain.usecase.ResultInsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class YandexMapScreenVM @Inject constructor(
    val appCache: AppCache,
    private val resultInsertUseCase: ResultInsertUseCase

) : BaseVM() {

    fun insertResult(result:SearchResult){
        launchVM {
            resultInsertUseCase.insertResult(result)
        }
    }

}
package com.example.mapkit.app.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapkit.app.base.BaseVM
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.data.local.AppCache
import com.example.mapkit.domain.usecase.ResultDeleteUseCase
import com.example.mapkit.domain.usecase.ResultListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeScreenVM @Inject constructor(
    val appCache: AppCache,
    private val resultListUseCase: ResultListUseCase,
    private val resultDeleteUseCase: ResultDeleteUseCase
) : BaseVM() {

    private val _resultLiveData = MutableLiveData<List<SearchResult>>()
    val resultLiveData: LiveData<List<SearchResult>> = _resultLiveData

    init {
        getList()
    }

    fun delete(item:SearchResult){
        launchVM {
            resultDeleteUseCase.deleteResult(item)
        }
    }

    private fun getList(){
        launchVM {
            resultListUseCase.getResultList().collect{
                _resultLiveData.value = it
            }
        }
    }

}
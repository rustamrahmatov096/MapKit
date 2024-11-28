package com.example.mapkit.data.usecase

import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.domain.repository.SearchResultRepository
import com.example.mapkit.domain.usecase.ResultInsertUseCase
import javax.inject.Inject

class ResultInsertUseCaseImpl @Inject constructor(
    private val repository: SearchResultRepository
) : ResultInsertUseCase {

    override suspend fun insertResult(widget: SearchResult) {
        repository.insertResult(widget)
    }

}
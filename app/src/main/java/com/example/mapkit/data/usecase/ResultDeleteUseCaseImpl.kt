package com.example.mapkit.data.usecase

import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.domain.repository.SearchResultRepository
import com.example.mapkit.domain.usecase.ResultDeleteUseCase
import javax.inject.Inject

class ResultDeleteUseCaseImpl @Inject constructor(
    private val repository: SearchResultRepository
) : ResultDeleteUseCase {

    override suspend fun deleteResult(result: SearchResult) {
        repository.deleteResult(result)
    }

}
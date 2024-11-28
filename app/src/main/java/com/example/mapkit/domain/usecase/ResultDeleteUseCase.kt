package com.example.mapkit.domain.usecase

import com.example.mapkit.app.model.SearchResult

interface ResultDeleteUseCase {
    suspend fun deleteResult(result: SearchResult)
}
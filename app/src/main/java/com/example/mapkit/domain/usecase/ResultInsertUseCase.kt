package com.example.mapkit.domain.usecase

import com.example.mapkit.app.model.SearchResult

interface ResultInsertUseCase {
    suspend fun insertResult(result: SearchResult)
}
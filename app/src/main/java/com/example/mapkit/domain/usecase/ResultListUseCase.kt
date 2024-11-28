package com.example.mapkit.domain.usecase

import com.example.mapkit.app.model.SearchResult
import kotlinx.coroutines.flow.Flow


interface ResultListUseCase {
    fun getResultList(): Flow<List<SearchResult>>
}
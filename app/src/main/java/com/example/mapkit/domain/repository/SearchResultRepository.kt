package com.example.mapkit.domain.repository

import com.example.mapkit.app.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchResultRepository {

    fun getResultList(): Flow<List<SearchResult>>

    suspend fun insertResult(widget: SearchResult)

    suspend fun deleteResult(widget: SearchResult)
}
package com.example.mapkit.data.repository

import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.domain.repository.SearchResultRepository
import com.example.mapkit.domain.source.SearchListDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val dataSource: SearchListDS
) : SearchResultRepository {

    override fun getResultList(): Flow<List<SearchResult>> {
        return dataSource.getResultList()
    }

    override suspend fun insertResult(widget: SearchResult) =
        withContext(Dispatchers.IO) {
            dataSource.insertResult(widget)
        }

    override suspend fun deleteResult(widget: SearchResult) =
        withContext(Dispatchers.IO) {
            dataSource.deleteResult(widget)
        }
}
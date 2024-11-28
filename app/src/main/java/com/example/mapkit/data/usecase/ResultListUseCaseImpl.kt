package com.example.mapkit.data.usecase

import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.domain.repository.SearchResultRepository
import com.example.mapkit.domain.usecase.ResultListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResultListUseCaseImpl @Inject constructor(
    private val repository: SearchResultRepository
) : ResultListUseCase {

    override fun getResultList(): Flow<List<SearchResult>> {
        return repository.getResultList()
    }

}
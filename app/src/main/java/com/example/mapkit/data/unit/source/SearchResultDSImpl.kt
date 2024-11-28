package com.example.mapkit.data.unit.source

import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.data.models.api.local.dao.SearchResultDao
import com.example.mapkit.data.models.api.mapper.SearchResultEntityMapper
import com.example.mapkit.domain.source.SearchListDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SearchResultDSImpl @Inject constructor(
    private val dao: SearchResultDao,
    private val mapper: SearchResultEntityMapper
) : SearchListDS {

    override  fun getResultList(): Flow<List<SearchResult>> {
        return channelFlow {
            dao.getAllAddresses().collectLatest {
                mapper.mapListEntity(it)?.let { it1 -> send(it1) }
            }
        }
    }

    override suspend fun insertResult(widget: SearchResult) {
        dao.insertAddress(mapper.mapData(widget))
    }

    override suspend fun deleteResult(widget: SearchResult) {
        dao.deleteAddress(mapper.mapData(widget))
    }


}
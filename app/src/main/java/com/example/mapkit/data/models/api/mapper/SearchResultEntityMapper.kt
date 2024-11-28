package com.example.mapkit.data.models.api.mapper

import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.data.models.api.local.entity.SearchResultEntity
import com.example.mapkit.data.unit.Mapper
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject

class SearchResultEntityMapper @Inject constructor() : Mapper<SearchResultEntity, SearchResult>() {

    override fun mapData(dto: SearchResult): SearchResultEntity {
        return SearchResultEntity(
            id = dto.id,
            name = dto.name,
            address = dto.address,
            distance = dto.distance,
            long = dto.point?.longitude,
            lat = dto.point?.latitude
        )
    }

    override fun mapEntity(dto: SearchResultEntity): SearchResult {
        return SearchResult(
            id = dto.id,
            name = dto.name,
            address = dto.address,
            distance = dto.distance,
            point = dto.lat?.let { dto.long?.let { it1 -> Point(it, it1) } }
        )
    }


}
package com.example.mapkit.data.models.api.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_result")
data class SearchResultEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String? =null,
    @ColumnInfo(name = "address") var address: String? =null,
    @ColumnInfo(name = "distance") var distance: String?=null,
    @ColumnInfo(name = "long") var long: Double?=null,
    @ColumnInfo(name = "lat") var lat: Double?=null
)
package com.example.mapkit.data.models.api.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mapkit.data.models.api.local.entity.SearchResultEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SearchResultDao {


        @Insert
        suspend fun insertAddress(result: SearchResultEntity)

        @Delete
        suspend fun deleteAddress(address: SearchResultEntity)

        @Query("SELECT * FROM search_result")
        fun getAllAddresses(): Flow<List<SearchResultEntity>>


}
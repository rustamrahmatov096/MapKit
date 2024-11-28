package com.example.mapkit.data.di

import com.example.mapkit.data.unit.source.SearchResultDSImpl
import com.example.mapkit.domain.source.SearchListDS
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindsSearchResultDataSource(source: SearchResultDSImpl): SearchListDS

}
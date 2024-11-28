package com.example.mapkit.data.di

import com.example.mapkit.data.local.AppCache
import com.example.mapkit.data.local.AppCacheImpl
import com.example.mapkit.data.repository.SearchResultRepositoryImpl
import com.example.mapkit.domain.repository.SearchResultRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsAppCatch(appCacheImpl: AppCacheImpl): AppCache

    @Binds
    fun bindsSearchResultRepository(repository: SearchResultRepositoryImpl): SearchResultRepository

}
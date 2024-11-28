package com.example.mapkit.data.di

import com.example.mapkit.data.local.AppDatabase
import com.example.mapkit.data.models.api.local.dao.SearchResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideWalletDao(db: AppDatabase): SearchResultDao {
        return db.searchResultDao()
    }

}


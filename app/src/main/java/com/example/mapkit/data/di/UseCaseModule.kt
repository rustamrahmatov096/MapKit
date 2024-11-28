package com.example.mapkit.data.di

import com.example.mapkit.data.usecase.ResultDeleteUseCaseImpl
import com.example.mapkit.data.usecase.ResultInsertUseCaseImpl
import com.example.mapkit.data.usecase.ResultListUseCaseImpl
import com.example.mapkit.domain.usecase.ResultDeleteUseCase
import com.example.mapkit.domain.usecase.ResultInsertUseCase
import com.example.mapkit.domain.usecase.ResultListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindsResultListUseCase(case: ResultListUseCaseImpl): ResultListUseCase

    @Binds
    fun bindsResultInsertUseCase(case: ResultInsertUseCaseImpl): ResultInsertUseCase

    @Binds
    fun bindsResultDeleteUseCase(case: ResultDeleteUseCaseImpl): ResultDeleteUseCase

}
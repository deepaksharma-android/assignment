package com.example.assignment.di

import com.example.assignment.data.remote.api.HoldingsApi
import com.example.assignment.data.repository.HoldingsRepositoryImpl
import com.example.assignment.domain.repository.HoldingsRepository
import com.example.assignment.domain.usecase.GetHoldingsUseCase
import com.example.assignment.domain.usecase.GetHoldingsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingsModule {
    @Binds
    @Singleton
    abstract fun bindHoldingsRepository(impl: HoldingsRepositoryImpl): HoldingsRepository

    @Binds
    @Singleton
    abstract fun bindGetHoldingsUseCase(impl: GetHoldingsUseCaseImpl): GetHoldingsUseCase
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideHoldingsApi(retrofit: Retrofit): HoldingsApi =
        retrofit.create(HoldingsApi::class.java)
}



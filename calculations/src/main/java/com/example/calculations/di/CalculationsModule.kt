package com.example.calculations.di

import com.example.calculations.CalculationsProviderImpl
import com.example.calculations.data.repository.DepositRepository
import com.example.domain.interfaces.CalculationsProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalculationsModule {
    @Provides
    @Singleton
    fun provideCalculationsProvider(
        repository: DepositRepository
    ): CalculationsProvider {
        return CalculationsProviderImpl(repository)
    }
}
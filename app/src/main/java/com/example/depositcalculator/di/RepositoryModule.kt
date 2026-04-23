package com.example.depositcalculator.di

import com.example.depositcalculator.data.repository.DepositRepositoryImpl
import com.example.depositcalculator.domain.repository.DepositRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDepositRepository(impl: DepositRepositoryImpl): DepositRepository
}
package com.example.auth.di

import com.example.auth.AuthManagerImpl
import com.example.auth.data.network.TokenManager
import com.example.auth.data.repository.AuthRepository
import com.example.domain.interfaces.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(
        tokenManager: TokenManager,
        authRepository: AuthRepository
    ): AuthManager {
        return AuthManagerImpl(tokenManager, authRepository)
    }
}
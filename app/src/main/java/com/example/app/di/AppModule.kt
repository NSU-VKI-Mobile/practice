package com.example.app.di

import com.example.app.navigation.AppAuthNavigator
import com.example.app.navigation.AppCalculationsNavigator
import com.example.domain.interfaces.AuthNavigator
import com.example.domain.interfaces.CalculationsNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthNavigator(): AuthNavigator = AppAuthNavigator()

    @Provides
    @Singleton
    fun provideCalculationsNavigator(): CalculationsNavigator = AppCalculationsNavigator()

}
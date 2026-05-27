package ci.nsu.mobile.app.di

import ci.nsu.mobile.auth.data.domain.AuthManagerImpl
import ci.nsu.mobile.auth.navigation.AuthNavigatorImpl
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.auth.AuthNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppAuthBindModule {

    @Binds
    @Singleton
    abstract fun bindAuthManager(
        authManagerImpl: AuthManagerImpl
    ): AuthManager

    @Binds
    @Singleton
    abstract fun bindAuthNavigator(
        authNavigatorImpl: AuthNavigatorImpl
    ): AuthNavigator
}
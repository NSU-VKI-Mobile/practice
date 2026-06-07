package ci.nsu.mobile.auth.di

import ci.nsu.mobile.auth.navigation.AuthManagerImpl
import ci.nsu.mobile.auth.navigation.AuthNavigatorImpl
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.AuthNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    @Singleton
    abstract fun bindAuthManager(
        impl: AuthManagerImpl
    ): AuthManager

    @Binds
    @Singleton
    abstract fun bindAuthNavigator(
        impl: AuthNavigatorImpl
    ): AuthNavigator
}
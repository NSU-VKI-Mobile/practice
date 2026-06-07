package ci.nsu.mobile.auth.di

import ci.nsu.mobile.auth.data.TokenManagerImpl
import ci.nsu.mobile.domain.token.ITokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(
        impl: TokenManagerImpl
    ): ITokenManager
}
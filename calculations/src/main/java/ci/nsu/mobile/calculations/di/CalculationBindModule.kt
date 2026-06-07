package ci.nsu.mobile.calculations.di

import ci.nsu.mobile.calculations.navigation.CalculationsNavigatorImpl
import ci.nsu.mobile.domain.interfaces.CalculationsNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CalculationsBindModule {

    @Binds
    @Singleton
    abstract fun bindCalculationsNavigator(
        impl: CalculationsNavigatorImpl
    ): CalculationsNavigator
}
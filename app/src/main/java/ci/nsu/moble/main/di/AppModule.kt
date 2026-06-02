package ci.nsu.moble.main.di

import ci.nsu.moble.domain.interfaces.AuthNavigator
import ci.nsu.moble.domain.interfaces.CalculationsNavigator
import ci.nsu.moble.main.navigation.AuthNavigatorImpl
import ci.nsu.moble.main.navigation.CalculationsNavigatorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::AuthNavigatorImpl) bind AuthNavigator::class
    singleOf(::CalculationsNavigatorImpl) bind CalculationsNavigator::class
}
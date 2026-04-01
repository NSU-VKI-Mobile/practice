package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализируем хранилище сессии
        TokenManager.init(this)

        // Запускаем внедрение зависимостей
        startKoin {
            androidLogger() // Включаем логирование Koin (поможет, если что-то упадет)
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
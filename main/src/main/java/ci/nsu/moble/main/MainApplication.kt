package ci.nsu.moble.main

import android.app.Application
import ci.nsu.moble.main.di.appModule // Импорт вашего модуля Koin с зависимостями
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Говорим приложению: "При старте запусти Koin и загрузи наши объекты"
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule) // Тот самый модуль, где лежат ваши ViewModel и Retrofit
        }
    }
}
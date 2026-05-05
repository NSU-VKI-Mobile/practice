package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.auth.di.authModule
import ci.nsu.mobile.calculations.di.calculationsModule
import ci.nsu.mobile.main.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule, authModule, calculationsModule)
        }
    }
}

package ci.nsu.moble.main

import android.app.Application
import ci.nsu.moble.auth.di.authModule
import ci.nsu.moble.calculations.di.calculationsModule
import ci.nsu.moble.main.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(
                authModule,
                calculationsModule,
                appModule
            )
        }
    }
}
package ci.nsu.mobile.app

import android.app.Application
import ci.nsu.mobile.app.di.ServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
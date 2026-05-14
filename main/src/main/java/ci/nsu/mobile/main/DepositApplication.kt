package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.di.ServiceLocator

class DepositApplication : Application() {

    lateinit var locator: ServiceLocator
        private set

    override fun onCreate() {
        super.onCreate()
        locator = ServiceLocator(this)
    }
}
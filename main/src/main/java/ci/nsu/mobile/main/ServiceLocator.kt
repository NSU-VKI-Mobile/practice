package ci.nsu.mobile.main

import android.content.Context
import ci.nsu.mobile.main.dao.DepositDao
import ci.nsu.mobile.main.data.api.ApiClient
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.db.AppDatabase

class ServiceLocator private constructor(context: Context) {

    val authRepository: AuthRepository by lazy {
        AuthRepository(ApiClient.api)
    }

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    val depositDao: DepositDao by lazy {
        database.depositDao()
    }

    companion object {
        @Volatile
        private var instance: ServiceLocator? = null

        fun init(context: Context) {
            if (instance == null) synchronized(this) {
                if (instance == null)
                    instance = ServiceLocator(context.applicationContext)
            }
        }

        fun get(): ServiceLocator =
            instance ?: error("ServiceLocator не инициализирован!!!")
    }
}
package com.example.integratedapp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.integratedapp.data.db.AppDatabase
import com.example.integratedapp.data.repository.AuthRepository
import com.example.integratedapp.data.repository.DepositRepository
import com.example.integratedapp.ui.auth.AuthViewModel
import com.example.integratedapp.ui.calculations.CalculationsViewModel
import com.example.integratedapp.ui.newcalc.NewCalcViewModel
import com.example.integratedapp.ui.users.UsersViewModel

// ============================================
// Service Locator — паттерн внедрения зависимостей
// ============================================
//
// Что это:
// Один объект который умеет создавать и хранить ВСЕ зависимости приложения
// (базу, репозитории, ViewModel-и).
//
// Зачем это нужно:
// Без Service Locator каждый ViewModel должен сам создавать репозиторий,
// репозиторий — базу, и т.д. Получается много повторяющегося кода.
// С Service Locator всё создаётся один раз в одном месте.
//
// Аналогия:
// Service Locator = склад деталей. Программист (ViewModel) приходит на склад
// и говорит: "дайте мне репозиторий". Не нужно знать как он собран.

object ServiceLocator {

    // by lazy — создаётся только при первом обращении, потом возвращает тот же объект
    // Это паттерн "ленивая инициализация" — экономит память
    // База создаётся только когда впервые понадобилась
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Все зависимости — синглтоны через `by lazy`
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(appContext)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(database)
    }

    // Фабрика для создания всех ViewModel
    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory()
    }
}

// Универсальная фабрика для всех ViewModel
// Знает как создать каждый ViewModel с нужными зависимостями
class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(ServiceLocator.authRepository) as T

            modelClass.isAssignableFrom(UsersViewModel::class.java) ->
                UsersViewModel(ServiceLocator.authRepository) as T

            modelClass.isAssignableFrom(CalculationsViewModel::class.java) ->
                CalculationsViewModel(ServiceLocator.depositRepository) as T

            modelClass.isAssignableFrom(NewCalcViewModel::class.java) ->
                NewCalcViewModel(ServiceLocator.depositRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

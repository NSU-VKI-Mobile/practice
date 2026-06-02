package ci.nsu.mobile.main.viewmodel

//ViewModelFactory - это фабрика, которая создает ViewModel, когда у ViewModel есть параметры в конструкторе.
//параметр - AuthRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.repository.AuthRepository

class LoginViewModelFactory(
    private val authRepository: AuthRepository // Сохраняем репозиторий
) : ViewModelProvider.Factory {  // Реализуем интерфейс Factory
    //Главный метод фабрики - создает ViewModel
    //modelClass - класс ViewModel, которую нужно создать
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //// Проверяем, что запросили именно LoginViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
            // Создаем LoginViewModel, передавая в конструктор authRepository
            // as T - приводим к нужному типу
        }
        // Если запросили какой-то другой ViewModel - ошибка
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
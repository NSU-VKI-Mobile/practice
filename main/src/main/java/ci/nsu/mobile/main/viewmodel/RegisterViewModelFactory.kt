package ci.nsu.mobile.main.viewmodel

//ViewModelFactory - это фабрика, которая создает ViewModel, когда у ViewModel есть параметры в конструкторе.
//параметр - AuthRepository
//RegisterViewModelFactory - создает RegisterViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.repository.AuthRepository

class RegisterViewModelFactory(
    private val authRepository: AuthRepository // Сохраняем репозиторий
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Проверяем, что запросили именно RegisterViewModel
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            // Создаем RegisterViewModel, передавая authRepository
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        // Если запросили какой-то другой ViewModel - ошибка
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
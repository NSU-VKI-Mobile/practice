package ci.nsu.mobile.main.viewmodel

//ViewModelFactory - это фабрика, которая создает ViewModel, когда у ViewModel есть параметры в конструкторе.
//параметр - AuthRepository
//UsersViewModelFactory - создает UsersViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.repository.AuthRepository

class UsersViewModelFactory(
    private val authRepository: AuthRepository  // Сохраняем репозиторий
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Проверяем, что запросили именно UsersViewModel
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            // Создаем UsersViewModel, передавая authRepository
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(authRepository) as T
        }
        // Если запросили какой-то другой ViewModel - ошибка
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.repository.AuthRepository

class UserListViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
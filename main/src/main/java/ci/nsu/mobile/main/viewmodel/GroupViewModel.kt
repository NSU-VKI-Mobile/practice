package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto  // <-- Добавьте
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GroupListState {
    object Loading : GroupListState()
    data class Success(val groups: List<GroupDto>) : GroupListState()
    data class Error(val message: String) : GroupListState()
}

class GroupViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _groupsState = MutableStateFlow<GroupListState>(GroupListState.Loading)
    val groupsState: StateFlow<GroupListState> = _groupsState

    fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = GroupListState.Loading
            val result = repository.getGroups()
            _groupsState.value = if (result.isSuccess) {
                GroupListState.Success(result.getOrNull() ?: emptyList())
            } else {
                GroupListState.Error(result.exceptionOrNull()?.message ?: "Failed to load groups")
            }
        }
    }
}
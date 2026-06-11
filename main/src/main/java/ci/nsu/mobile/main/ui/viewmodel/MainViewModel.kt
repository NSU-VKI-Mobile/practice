class MainViewModel : ViewModel() {
    private val repository = AuthRepository()

    var users by mutableStateOf<List<UserDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init { loadUsers() }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            repository.getUsers()
                .onSuccess { users = it }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun logout() {
        TokenManager.clear()
        // Сигнал для навигации
    }
}
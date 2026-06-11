class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun login() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.login(login, password)
                .onSuccess { loginSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}
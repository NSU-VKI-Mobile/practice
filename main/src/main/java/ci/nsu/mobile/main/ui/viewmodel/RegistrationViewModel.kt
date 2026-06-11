class RegistrationViewModel : ViewModel() {
    private val repository = AuthRepository()

    var groups by mutableStateOf<List<GroupDto>>(emptyList())
    var selectedGroupId by mutableStateOf<Int?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var registrationSuccess by mutableStateOf(false)

    // поля для PersonDto
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var middleName by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var gender by mutableStateOf("")
    // поля для RegisterRequest
    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups = it }
                .onFailure { errorMessage = it.message }
        }
    }

    fun register() {
        val groupId = selectedGroupId ?: return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val person = PersonDto(firstName, lastName, middleName, birthDate, gender, groupId)
            val request = RegisterRequest(login, password, email, phoneNumber, person = person)
            repository.register(request)
                .onSuccess { registrationSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}
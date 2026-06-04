package ci.nsu.mobile.main.model

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<LoginResponse>
    suspend fun register(request: RegisterRequest): Result<Unit>
    suspend fun getUsers(): Result<List<UserDto>>
    suspend fun getGroups(): Result<List<GroupDto>>
}


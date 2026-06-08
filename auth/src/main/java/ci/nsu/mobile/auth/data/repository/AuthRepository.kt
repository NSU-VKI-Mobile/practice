package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.data.network.ApiService
import ci.nsu.mobile.auth.data.network.models.GroupDto
import ci.nsu.mobile.auth.data.network.models.LoginRequest
import ci.nsu.mobile.auth.data.network.models.RegisterRequest
import ci.nsu.mobile.domain.models.User
import ci.nsu.mobile.domain.token.ITokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val service: ApiService,
    private val tokenManager: ITokenManager
) {
    suspend fun login(login: String, password: String): Result<User> {
        return try {
            tokenManager.clear()
            android.util.Log.d("AUTH", "After clear - userId: ${tokenManager.userId}")
            val response = service.loginUser(LoginRequest(login = login, password = password))
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception("Ошибка входа: ${response.code()} ${response.message()}"))
            }
            val authResponse = response.body()!!
            tokenManager.token = authResponse.token

            val usersResponse = service.getUsers()
            if (!usersResponse.isSuccessful || usersResponse.body() == null) {
                tokenManager.clear()
                return Result.failure(Exception("Ошибка получения пользователей"))
            }

            val currentUserDto = usersResponse.body()!!.find { it.login == login }
            if (currentUserDto != null) {
                tokenManager.userId = currentUserDto.userId
                tokenManager.userLogin = currentUserDto.login
                tokenManager.userEmail = currentUserDto.email
                tokenManager.userPersonId = currentUserDto.personId
                tokenManager.userCreatedDate = currentUserDto.createdDate
                tokenManager.userPhone = currentUserDto.phoneNumber
                tokenManager.userRoleId = currentUserDto.roleId
                tokenManager.userLastLoginDate = currentUserDto.lastLoginDate

                val currentUser = User(
                    userId = currentUserDto.userId,
                    login = currentUserDto.login,
                    email = currentUserDto.email,
                    phoneNumber = currentUserDto.phoneNumber,
                    roleId = currentUserDto.roleId,
                    authAllowed = currentUserDto.authAllowed,
                    personId = currentUserDto.personId,
                    createdDate = currentUserDto.createdDate,
                    lastLoginDate = currentUserDto.lastLoginDate
                )
                Result.success(currentUser)

            } else {
                tokenManager.clear()
                Result.failure(Exception("Пользователь не найден в системе"))
            }

        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
        android.util.Log.d("AUTH", "After save - userId: ${tokenManager.userId}, login: ${tokenManager.userLogin}")
    }

    suspend fun register(registerRequest: RegisterRequest): Result<User> {
        return try {
            val response = service.registerUser(registerRequest)
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception("Ошибка регистрации: ${response.code()} ${response.message()}"))
            }
            val authResponse = response.body()!!
            tokenManager.token = authResponse.token
            tokenManager.userLogin = registerRequest.login
            val usersResponse = service.getUsers()
            if (!usersResponse.isSuccessful || usersResponse.body() == null) {
                return Result.failure(Exception("Ошибка получения пользователей"))
            }
            val currentUser = usersResponse.body()!!.find { it.login == registerRequest.login }
            if (currentUser != null) {
                tokenManager.userId = currentUser.userId
                Result.success(currentUser)
            } else {
                tokenManager.clear()
                Result.failure(Exception("Пользователь не найден после регистрации"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }

    suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = service.getUsers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка получения пользователей"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = service.getGroups()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка получения групп"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }
}
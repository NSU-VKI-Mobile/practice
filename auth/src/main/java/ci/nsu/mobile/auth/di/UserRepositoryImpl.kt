package ci.nsu.mobile.auth.di

import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.domain.interfaces.UserRepository
import ci.nsu.mobile.domain.models.User

class UserRepositoryImpl(
    private val authRepository: AuthRepository
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        val result = authRepository.getUsers()
        return when (result) {
            is ci.nsu.mobile.auth.data.repository.ApiResult.Success -> {
                result.data.map { userDto ->
                    User(
                        id = userDto.id,
                        login = userDto.login,
                        email = userDto.email,
                        phoneNumber = userDto.phoneNumber,
                        firstName = userDto.person?.firstName,
                        lastName = userDto.person?.lastName
                    )
                }
            }
            else -> emptyList()
        }
    }
}
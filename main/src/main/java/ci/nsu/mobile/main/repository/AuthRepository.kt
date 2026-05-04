package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.network.dtos.UserDto

class AuthRepository {
    fun login(login: String, password: String): Result<UserDto> {
        val user = UserDto(login = login, password = password)
        return Result.success(user)
    }
//    fun register(registerRequest: RegisterRequest): Result<Unit> {
//        return
//    }
//    fun getUsers(): Result<List<UserDto>> {
//        return
//    }
//    fun getGroups(): Result<List<GroupDto>> {
//        return
//    }

}
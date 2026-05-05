package ci.nsu.mobile.main.data.repository

import javax.inject.Inject


import ci.nsu.mobile.main.core.network.api.ApiClient
import ci.nsu.mobile.main.model.dto.GroupDto

import ci.nsu.mobile.main.model.dto.UserDto


class UserRepository @Inject constructor(
    private val api: ApiClient,
) {
    suspend fun getUsers(): Result<List<UserDto>> {
        return runCatching {
            api.getUsers()
        }
    }

}
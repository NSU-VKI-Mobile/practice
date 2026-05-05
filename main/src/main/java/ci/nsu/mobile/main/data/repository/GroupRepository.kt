package ci.nsu.mobile.main.data.repository

import javax.inject.Inject

import ci.nsu.mobile.main.core.network.api.ApiClient
import ci.nsu.mobile.main.model.dto.AuthResponse

import ci.nsu.mobile.main.model.dto.GroupDto
import ci.nsu.mobile.main.model.dto.RegisterRequest


class GroupRepository @Inject constructor(
    private val api: ApiClient,
) {
    suspend fun getGroups(): Result<List<GroupDto>> {
        return runCatching {
            api.getGroups()
        }
    }

}
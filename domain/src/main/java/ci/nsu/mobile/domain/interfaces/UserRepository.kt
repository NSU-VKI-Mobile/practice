package ci.nsu.mobile.domain.interfaces

import ci.nsu.mobile.domain.models.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}
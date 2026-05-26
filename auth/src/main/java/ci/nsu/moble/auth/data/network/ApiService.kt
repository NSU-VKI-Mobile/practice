package ci.nsu.moble.auth.data.network

import ci.nsu.moble.domain.models.Group
import ci.nsu.moble.domain.models.RegisterData
import ci.nsu.moble.domain.models.User
import kotlinx.serialization.InternalSerializationApi
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<AuthResponse>

    @OptIn(InternalSerializationApi::class)
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterData
    ): Response<Unit>

    @OptIn(InternalSerializationApi::class)
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @OptIn(InternalSerializationApi::class)
    @GET("groups")
    suspend fun getGroups(): Response<List<Group>>
}

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String
)
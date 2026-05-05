package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.models.*
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}
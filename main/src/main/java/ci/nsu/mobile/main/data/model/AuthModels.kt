package ci.nsu.mobile.main.data.model


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: Long
)

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)

data class RegisterResponse(
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Int?
)
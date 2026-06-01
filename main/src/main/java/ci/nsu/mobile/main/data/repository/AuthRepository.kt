package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.api.ApiClient
import ci.nsu.mobile.main.api.requestData.AuthTokenRespone
import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.api.requestData.LoginRequest
import ci.nsu.mobile.main.api.requestData.RegisterRequest
import ci.nsu.mobile.main.data.dto.UserDto

class AuthRepository (){
    private val api = ApiClient.api

    suspend fun login(login: String, password: String): Result<AuthTokenRespone> {
        try {
            val response = api.login(LoginRequest(login, password))
            return if (response.isSuccessful) {
                val answerReq = response.body()
                if(answerReq != null) {
                    Result.success(answerReq)
                }
                else{
                    Result.failure(Exception("Пустой ответ от сервера"))
                }
            } else {
                Result.failure(Exception("Неправильный логин или пароль"))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit>{
        try{
            val response = api.register(registerRequest)
            return if(response.isSuccessful){
                Result.success(Unit)
            }else{
                Result.failure(Exception("Ошибка регистрации: ${response.errorBody()?.string()}"))
            }
        }catch (e: Exception){
            return Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }

    suspend fun getUsers(): Result<List<UserDto>>{
        try{
            val response = api.getUsers()
            return if(response.isSuccessful){
                Result.success(response.body() ?: emptyList())
            }else{
                Result.failure(Exception("Ошибка загрузки: ${response.errorBody()?.string()}"))
            }
        }catch (e: Exception){
            return Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>>{
        try{
            val response = api.getGroups()
            return if(response.isSuccessful){
                Result.success(response.body() ?: emptyList())
            }else{
                Result.failure(Exception("Ошибка загрузки: ${response.errorBody()?.string()}"))
            }
        }catch (e: Exception){
            return Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }
}
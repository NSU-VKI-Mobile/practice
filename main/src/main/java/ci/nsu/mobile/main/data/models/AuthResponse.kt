package ci.nsu.mobile.main.data.models
//ответ сервера при входе
data class AuthResponse(
    val token: String
)
//После успешного входа сервер возвращает токен
//Токен мы сохраняем и используем для авторизации следующих запросов
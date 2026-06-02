package ci.nsu.mobile.main.utils

import ci.nsu.mobile.main.data.models.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

/**
 * Класс для преобразования ошибок сервера в понятные пользователю сообщения
 */
object ErrorHandler {

    /**
     * Преобразует исключение в понятное сообщение для пользователя
     */
    fun getReadableErrorMessage(exception: Throwable): String {
        return when (exception) {
            is HttpException -> handleHttpException(exception)
            is java.net.SocketTimeoutException -> "Сервер не отвечает. Проверьте подключение к интернету и попробуйте снова."
            is java.net.UnknownHostException -> "Не удалось подключиться к серверу. Проверьте адрес сервера."
            is java.io.IOException -> "Проблема с интернет-соединением. Проверьте подключение."
            else -> exception.message ?: "Произошла неизвестная ошибка"
        }
    }

    /**
     * Обрабатывает HTTP ошибки (4xx, 5xx)
     */
    private fun handleHttpException(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string()
        val httpCode = e.code()

        println("=== ДЕТАЛИ ОШИБКИ ОТ СЕРВЕРА ===")
        println("HTTP код: $httpCode")
        println("Тело ошибки: $errorBody")

        // Специальная обработка для ошибки "Failed to read request"
        if (errorBody?.contains("Failed to read request") == true) {
            return "❌ Сервер не может обработать запрос. Возможные причины:\n" +
                    "   • Дата рождения: должна быть в формате ГГГГ-ММ-ДД (например, 2000-01-31)\n" +
                    "   • Пол: только MALE (мужской) или FEMALE (женский)\n" +
                    "   • Все поля должны быть заполнены\n" +
                    "   • Email должен быть в формате user@example.com\n" +
                    "   • Телефон должен содержать только цифры и знак +"
        }

        // Пытаемся распарсить ошибку от сервера
        val parsedError = parseErrorBody(errorBody)

        // Если есть поле fieldErrors - значит ошибка валидации конкретных полей
        if (!parsedError.fieldErrors.isNullOrEmpty()) {
            return buildFieldErrorMessage(parsedError.fieldErrors)
        }

        // Если есть detail или description - используем их
        val serverMessage = parsedError.detail
            ?: parsedError.description
            ?: parsedError.message
            ?: parsedError.error
            ?: parsedError.title

        if (!serverMessage.isNullOrEmpty()) {
            return when {
                serverMessage.contains("birthDate", ignoreCase = true) ->
                    "❌ Неверный формат даты рождения.\n   Нужно: ГГГГ-ММ-ДД (например, 2000-01-31)\n   Вы ввели: 10-10-2007 → правильно: 2007-10-10"
                serverMessage.contains("email", ignoreCase = true) ->
                    "❌ Неверный формат email.\n   Пример: name@example.com"
                serverMessage.contains("phone", ignoreCase = true) ->
                    "❌ Неверный формат телефона.\n   Пример: +7 912 345-67-89"
                serverMessage.contains("gender", ignoreCase = true) ->
                    "❌ Неверный формат пола.\n   Используйте MALE (мужской) или FEMALE (женский) заглавными буквами"
                serverMessage.contains("login", ignoreCase = true) && httpCode == 409 ->
                    "❌ Пользователь с таким логином уже существует.\n   Придумайте другой логин"
                serverMessage.contains("email", ignoreCase = true) && httpCode == 409 ->
                    "❌ Пользователь с таким email уже существует.\n   Используйте другой email"
                serverMessage.contains("group", ignoreCase = true) ->
                    "❌ Не выбрана группа.\n   Пожалуйста, выберите группу из списка"
                else -> "$serverMessage"
            }
        }

        return getDefaultMessage(httpCode)
    }

    /**
     * Парсит JSON тело ошибки в объект ErrorResponse
     */
    private fun parseErrorBody(errorBody: String?): ErrorResponse {
        if (errorBody.isNullOrEmpty()) return ErrorResponse()

        return try {
            val gson = Gson()
            gson.fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: Exception) {
            ErrorResponse(message = errorBody)
        }
    }

    /**
     * Создает понятное сообщение из fieldErrors
     * Пример: поле "birthDate" -> "Дата рождения: неверный формат"
     */
    private fun buildFieldErrorMessage(fieldErrors: Map<String, List<String>>): String {
        val fieldNames = mapOf(
            "firstName" to "Имя",
            "lastName" to "Фамилия",
            "middleName" to "Отчество",
            "birthDate" to "Дата рождения",
            "gender" to "Пол",
            "login" to "Логин",
            "password" to "Пароль",
            "email" to "Email",
            "phoneNumber" to "Телефон",
            "groupId" to "Группа"
        )

        val messages = mutableListOf<String>()
        messages.add("⚠️ Проверьте следующие поля:\n")

        fieldErrors.forEach { (field, errors) ->
            val readableField = fieldNames[field] ?: field
            errors.forEach { error ->
                messages.add("• $readableField: ${humanizeError(field, error)}")
            }
        }

        return messages.joinToString("\n")
    }

    /**
     * Превращает техническую ошибку в понятную
     */
    private fun humanizeError(field: String, error: String): String {
        return when {
            error.contains("birthDate", ignoreCase = true) || error.contains("date", ignoreCase = true) ->
                "должна быть в формате ГГГГ-ММ-ДД (например, 2000-01-31)"
            error.contains("email", ignoreCase = true) ->
                "должен быть в формате example@mail.com"
            error.contains("phone", ignoreCase = true) ->
                "должен быть в формате +7 123 456-78-90"
            error.contains("gender", ignoreCase = true) ->
                "должен быть MALE (мужской) или FEMALE (женский) заглавными буквами"
            error.contains("required", ignoreCase = true) || error.contains("not blank", ignoreCase = true) ->
                "обязательно для заполнения"
            error.contains("size", ignoreCase = true) || error.contains("length", ignoreCase = true) ->
                "неверная длина. Проверьте количество символов"
            else -> error
        }
    }

    /**
     * Стандартные сообщения по коду HTTP
     */
    private fun getDefaultMessage(httpCode: Int): String {
        return when (httpCode) {
            400 -> "❌ Ошибка в данных. Проверьте:\n" +
                    "   • Дата рождения: должна быть в формате ГГГГ-ММ-ДД (например, 2000-01-31)\n" +
                    "   • Вы ввели: 10-10-2007, а нужно: 2007-10-10\n" +
                    "   • Пол: только MALE или FEMALE заглавными буквами\n" +
                    "   • Все поля должны быть заполнены\n" +
                    "   • Email должен быть в формате name@example.com"
            401, 403 -> "❌ Доступ запрещен"
            404 -> "❌ Сервис не найден"
            409 -> "❌ Пользователь с таким логином или email уже существует"
            422 -> "❌ Ошибка валидации. Проверьте формат данных"
            500, 502, 503 -> "❌ Проблема на сервере. Попробуйте позже"
            else -> "❌ Ошибка: $httpCode. Попробуйте позже"
        }
    }
}
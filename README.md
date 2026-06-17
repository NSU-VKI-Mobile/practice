# Лабораторная работа по Android-разработке: Интеграция с API и аутентификация

## Описание задания

Необходимо разработать Android-приложение для работы с REST API через безопасное соединение. Приложение должно реализовывать регистрацию, вход в систему, получение списка пользователей и групп с использованием аутентификации по токену (JWT). Реализация должна соответствовать архитектуре MVVM с использованием современных Kotlin-инструментов.

## Базовый URL

Для доступа к API использовать следующий базовый адрес:
```
var baseUrl: String = "http://192.168.200.160:8080/api/"
```

## Требования к разрешениям

### Разрешение на интернет

Убедитесь, что в файле `AndroidManifest.xml` добавлено разрешение на доступ к сети:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Это разрешение обязательно для выполнения HTTP-запросов к серверу.

## Технические требования

### 1. Настройка сети

Создать файл конфигурации безопасности сети:
- Путь: `app/src/main/res/xml/network_security_config.xml`
- Содержание: разрешить подключение к локальному IP-адресу (192.168.200.160) по HTTP

Пример содержимого:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.200.160</domain>
    </domain-config>
</network-security-config>
```

Убедиться, что в `AndroidManifest.xml` добавлена ссылка на этот файл:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

### 2. Архитектура MVVM

Реализовать паттерн Model-View-ViewModel с разделением на слои:
- **Repository** — для получения данных
- **ViewModel** — для управления состоянием UI
- **Compose (View)** — интерфейс пользовательский

### 3. AuthRepository

Создать `AuthRepository` для управления аутентификацией. Репозиторий должен предоставлять методы:
- `login(login: String, password: String): Result<UserDto>`
- `register(registerRequest: RegisterRequest): Result<Unit>`
- `getUsers(): Result<List<UserDto>>`
- `getGroups(): Result<List<GroupDto>>`

### 4. AuthInterceptor

Реализовать перехватчик запросов для добавления токена в заголовки:

```kotlin
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()

        val token = TokenManager.token
        requestBuilder.addHeader("Content-Type", "application/json")
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
```

### 5. TokenManager

Создать `TokenManager` для хранения токена в `SharedPreferences`.

## Доступные API-методы

- `GET /groups` — получить список групп
- `POST /auth/register` — регистрация нового пользователя
- `POST /auth/login` — вход в систему
- `GET /users` — получить список пользователей

## Модели данных

### GroupDto

```kotlin
@Serializable
data class GroupDto(
    @SerializedName("groupId")
    val id: Int,
    @SerializedName("groupName")
    val name: String
)
```

### PersonDto и RegisterRequest

Пример регистрации пользователя:

```kotlin
val person = PersonDto(
    firstName = firstName,
    lastName = lastName,
    middleName = patronymic,
    birthDate = dateOfBirth,
    gender = gender,
    groupId = groupId
)
val request = RegisterRequest(
    login = login,
    password = password,
    email = email,
    phoneNumber = phoneNumber,
    roleId = 1, // Always 1, not changeable
    authAllowed = true,
    person = person
)
```

## Экраны приложения

### 1. Экран входа
- Поля: логин, пароль
- Кнопка: "Войти"
- Ссылка: "Нет аккаунта? Зарегистрироваться"

### 2. Экран регистрации
- Поля: имя, фамилия, отчество, дата рождения, пол, группа (выпадающий список), логин, пароль, email, телефон
- Кнопка: "Зарегистрироваться"
- Выбор группы из доступных (получить через `getGroups`)

### 3. Главный экран (после входа)
- Отображение списка пользователей (получить через `getUsers`)
- Кнопка: "Выйти"

## Реализационные требования

- Использовать **Kotlin Serialization** для парсинга JSON
- Использовать **OkHttp** с `AuthInterceptor` для запросов
- Использовать **Ktor** или **Retrofit** для работы с API
- Хранить токен в `SharedPreferences` через `TokenManager`
- Обеспечить корректную обработку ошибок (сеть, валидация, авторизация)
- Обеспечить сохранение состояния при повороте экрана

## Дополнительно

- Добавить прогресс-бар при выполнении запросов
- Реализовать валидацию полей формы
- Использовать Material Design компоненты
- Обеспечить устойчивость к ошибкам сети

---

*Примечание: данное задание направлено на отработку навыков работы с сетью, аутентификацией, безопасным хранением данных и архитектурными паттернами в Android-приложениях.*
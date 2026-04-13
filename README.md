# Лабораторная работа по Android-разработке: Интеграция с API и аутентификация

## Описание задания

Необходимо разработать Android-приложение для работы с REST API через безопасное соединение. Приложение реализует регистрацию, вход в систему, получение списка пользователей и групп с использованием JWT-токена. Архитектурный подход — MVVM.

## Базовый URL

```kotlin
var baseUrl: String = "http://192.168.200.160:8080/api/"
```

## Требования к разрешениям

- В `AndroidManifest.xml` добавлено разрешение:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

- Добавлена конфигурация `network_security_config` для локального HTTP-хоста `192.168.200.160`.

## Реализовано в проекте

### 1) Сетевой слой

- `ApiService` (Retrofit) с методами:
  - `POST /auth/login`
  - `POST /auth/register`
  - `GET /users`
  - `GET /groups`
- `AuthInterceptor` добавляет:
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>` (если токен есть)
- `TokenManager` хранит JWT в `SharedPreferences`.

### 2) Архитектура MVVM

- `AuthRepository` инкапсулирует сетевые вызовы:
  - `login(login: String, password: String): Result<UserDto>`
  - `register(registerRequest: RegisterRequest): Result<Unit>`
  - `getUsers(): Result<List<UserDto>>`
  - `getGroups(): Result<List<GroupDto>>`
  - `logout()`
- Для каждого экрана используется свой ViewModel:
  - `LoginViewModel`
  - `RegisterViewModel`
  - `UsersViewModel`

### 3) Экраны приложения

1. **Экран входа (`LoginActivity`)**
   - Поля: логин, пароль
   - Кнопки: вход, переход на регистрацию

2. **Экран регистрации (`RegisterActivity`)**
   - Поля: имя, фамилия, отчество, дата рождения, пол, группа, логин, пароль, email, телефон
   - Группы загружаются из API (`getGroups`)
   - Кнопка регистрации отправляет `RegisterRequest`

3. **Главный экран (`MainActivity`)**
   - Отображает список пользователей (`getUsers`)
   - Кнопки: обновить список, выйти

## Модели данных

- `GroupDto`
- `UserDto`
- `PersonDto`
- `RegisterRequest`
- `LoginRequest`
- `LoginResponseDto`

Все DTO сериализуются через Kotlin Serialization.

## Технический стек

- Kotlin
- MVVM (ViewModel + LiveData)
- Retrofit + OkHttp
- Kotlin Serialization
- Coroutines

## Примечания по запуску

- API доступен в локальной сети колледжа.
- Без доступа к API UI и сборка работают, но сетевые запросы будут возвращать ошибки соединения.
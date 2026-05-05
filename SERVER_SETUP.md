# Server setup

## Что уже сделано

- Android-клиент настроен на `http://192.168.200.160:8080/api/`
- Для тестов добавлен локальный mock-сервер: [server/mock-api.js](/D:/practice2/server/mock-api.js)
- Доступные маршруты:
  - `GET /api/groups`
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - `GET /api/users`

## Как запустить mock-сервер

1. Откройте терминал в `D:\practice2`
2. Выполните:

```powershell
node .\server\mock-api.js
```

3. После запуска будут доступны:
   - тестовый пользователь: `student`
   - пароль: `1234`

## Чтобы Android-приложение увидело сервер

1. Сервер должен слушать `0.0.0.0:8080`
2. Компьютер с сервером должен иметь IP `192.168.200.160`
3. Телефон или эмулятор должны быть в той же сети
4. В брандмауэре Windows должен быть открыт входящий TCP-порт `8080`

## Если IP компьютера другой

Нужно заменить `API_BASE_URL` в [main/build.gradle.kts](/D:/practice2/main/build.gradle.kts) на фактический IP, например:

```kotlin
buildConfigField(
    "String",
    "API_BASE_URL",
    "\"http://192.168.1.15:8080/api/\""
)
```

## Важное замечание по сборке

Сейчас Gradle в системе запускается под Java 8, а проект требует Java 11 или новее.
Для сборки нужен JDK 11 или JDK 17.

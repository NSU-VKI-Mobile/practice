# Лабораторная работа №8: Модульная архитектура приложения

## Описание задания

На основе реализации из лабораторной работы №7 необходимо разделить приложение на два независимых модуля:
1. **Модуль аутентификации** (`:auth`)
2. **Модуль расчётов** (`:calculations`)

Реализовать модульную архитектуру с чётким разделением ответственности, интерфейсами для взаимодействия и независимой компиляцией модулей.

## Цели работы

- Отработка навыков проектирования модульной архитектуры
- Реализация слабой связанности между компонентами приложения
- Освоение механизмов взаимодействия между модулями
- Подготовка приложения к масштабированию и командной разработке

## Исходные условия

- База: приложение из лабораторной работы №7 в ветке `task-7`
- Новое решение реализуется в ветке **`task-8`**
- Сохранить функционал приложения в полном объёме

## Требования к модульной архитектуре

### 1. Структура проекта

```
app/
auth/
  src/
    main/
      java/
      res/
  build.gradle.kts
calculations/
  src/
    main/
      java/
      res/
  build.gradle.kts
domain/
  src/
    main/
      java/
  build.gradle.kts
:app (зависим от всех модулей)
```

### 2. Модуль аутентификации (`:auth`)

Содержит всю логику, связанную с авторизацией:

#### Включает:
- Экраны: вход, регистрация
- ViewModel: `AuthViewModel`, `RegisterViewModel`
- Репозиторий: `AuthRepository`
- Сетевые компоненты: API-сервисы, `AuthInterceptor`, `TokenManager`
- Модели данных: `UserDto`, `PersonDto`, `RegisterRequest`
- Навигацию внутри модуля

#### Предоставляет интерфейс:
```kotlin
interface AuthNavigator {
    fun navigateToLogin(context: Context)
    fun navigateToRegister(context: Context)
    fun openAuthFlow(activity: Activity, requestCode: Int)
}

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
}
```

### 3. Модуль расчётов (`:calculations`)

Содержит всю логику, связанную с расчётами вкладов:

#### Включает:
- Экраны: новый расчёт (двухэтапный), детали расчёта
- ViewModel: `DepositViewModel`, `CalculationViewModel`
- Репозиторий: `DepositRepository`
- Локальную базу данных: Room с `DepositCalculation`
- Модели данных: `DepositCalculation`, `CalculationResult`
- Навигацию внутри модуля

#### Предоставляет интерфейс:
```kotlin
interface CalculationsNavigator {
    fun navigateToNewCalculation(context: Context, userId: Long)
    fun navigateToMyCalculations(context: Context, userId: Long)
    fun openCalculationFlow(activity: Activity, userId: Long)
}

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    fun saveCalculation(calculation: DepositCalculation)
    fun deleteCalculation(calculationId: Long)
}
```

### 4. Общий модуль (`:domain`)

Создать общий модуль для совместно используемых компонентов:

#### Включает:
- Общие модели данных (если есть)
- Интерфейсы навигации и управления
- Общие утилиты и расширения
- Определения состояний (например, `sealed class AuthState`)

## Взаимодействие между модулями

### 1. Навигация

Реализовать навигацию через интерфейсы:
- Модуль `:app` знает о конкретных реализациях
- Модули `:auth` и `:calculations` зависят только от интерфейсов из `:domain`

Пример использования:
```kotlin
// В основном приложении
val authNavigator = AuthNavigatorImpl()
val calculationsNavigator = CalculationsNavigatorImpl()

// Передача навигаторов
bottomNavigationView.setOnItemSelectedListener { item ->
    when (item.itemId) {
        R.id.nav_users -> authNavigator.navigateToUsers(this)
        R.id.nav_calculations -> calculationsNavigator.navigateToMyCalculations(this, userId)
        R.id.nav_new_calc -> calculationsNavigator.navigateToNewCalculation(this, userId)
    }
}
```

### 2. Управление состоянием

- `:auth` управляет состоянием аутентификации
- `:calculations` управляет состоянием расчётов
- `:app` координирует взаимодействие

### 3. Внедрение зависимостей

Обновить механизм внедрения зависимостей для работы с модулями:

#### Для Koin:
```kotlin
// В auth/build.gradle.kts
val authModule = module {
    single<AuthNavigator> { AuthNavigatorImpl() }
    single<AuthManager> { AuthManagerImpl() }
}

// В calculations/build.gradle.kts
val calculationsModule = module {
    single<CalculationsNavigator> { CalculationsNavigatorImpl() }
    single<CalculationsProvider> { CalculationsProviderImpl(get()) }
}
```

#### Для Dagger Hilt:
- Использовать `@InstallIn(SingletonComponent::class)` для общих компонентов
- Создавать отдельные модули для каждого функционального блока

## Требования к реализации

### 1. Зависимости между модулями

- `:app` зависит от `:auth`, `:calculations`, `:domain`
- `:auth` и `:calculations` зависят от `:domain`
- `:auth` и `:calculations` **не зависят друг от друга**

### 2. API-поверхность модулей

Каждый модуль должен предоставлять чётко определённый API через интерфейсы, размещённые в `:domain`:
- Только интерфейсы в `:domain`
- Реализации остаются в конкретных модулях

### 3. Конфигурация сборки

Обновить `settings.gradle.kts`:
```kotlin
include(
    ":app",
    ":auth",
    ":calculations",
    ":domain"
)
```

Настроить `build.gradle.kts` для каждого модуля с правильными зависимостями.

## Проверка правильности модульности

### Тест на независимость

1. Временно отключить модуль `:calculations` в `settings.gradle.kts`
2. Убедиться, что модуль `:auth` компилируется самостоятельно
3. Повторить для модуля `:calculations`

### Тест на слабую связанность

1. Изменить реализацию навигации в одном модуле
2. Убедиться, что другой модуль не требует перекомпиляции
3. Проверить, что изменения интерфейсов ломают сборку (это корректное поведение)

## Реализационные требования

- Реализовать **чёткое разделение ответственности** между модулями
- Обеспечить **независимую компиляцию** модулей
- Использовать **интерфейсы для взаимодействия**
- Сохранить **всю функциональность** приложения
- Обеспечить **корректную работу навигации**
- Поддерживать **единую тему и дизайн**

## Дополнительно

- Добавить документацию к интерфесам API модулей
- Реализовать базовые unit-тесты для каждого модуля
- Настроить shared preferences/datastore для хранения состояния между сессиями
- Рассмотреть возможность выноса общих UI-компонентов в отдельный UI-модуль

---

*Примечание: данное задание направлено на отработку навыков проектирования масштабируемых Android-приложений с модульной архитектурой, подходящей для командной разработки и независимого развёртывания компонентов.*
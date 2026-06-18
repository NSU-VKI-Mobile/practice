# Design Document — Расчёт вкладов

## Overview

Приложение «Расчёт вкладов» — нативное Android-приложение, реализующее пошаговый ввод параметров банковского вклада, расчёт доходности по формуле сложных процентов с ежемесячным пополнением, отображение результата и хранение истории расчётов в локальной базе данных.

Архитектура — **MVVM** (Model-View-ViewModel) с паттерном Repository. Навигация между экранами управляется через **Navigation Component** (единый `nav_graph.xml`). Данные хранятся в **Room Database**. Реактивное обновление UI обеспечивается через **LiveData**. Все операции с базой данных выполняются в фоновом потоке через **Kotlin Coroutines**.

### Ключевые технические решения

| Решение | Обоснование |
|---|---|
| Single Activity + Fragments | Стандартный подход для Navigation Component; упрощает управление back stack |
| InputFragment с двумя шагами | Оба шага ввода живут в одном фрагменте с переключением видимости — один ViewModel сохраняет состояние обоих шагов |
| LiveData вместо StateFlow | Соответствует уровню курса; нативная интеграция с LifecycleOwner |
| Room Singleton | Гарантирует единственный экземпляр БД на весь жизненный цикл приложения |
| ViewBinding | Типобезопасный доступ к view без `findViewById` |

---

## Architecture

### Общая схема

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                         │
│  MainActivity                                           │
│  ├── MainFragment                                       │
│  ├── InputFragment  (Step1 + Step2, visibility toggle)  │
│  ├── ResultFragment                                     │
│  ├── HistoryFragment                                    │
│  └── DetailsFragment                                    │
└────────────────────────┬────────────────────────────────┘
                         │ observes LiveData
┌────────────────────────▼────────────────────────────────┐
│                    ViewModel Layer                      │
│  MainViewModel                                          │
│  InputViewModel   (shared между Step1 и Step2)          │
│  ResultViewModel                                        │
│  HistoryViewModel                                       │
│  DetailsViewModel                                       │
└────────────────────────┬────────────────────────────────┘
                         │ calls suspend functions
┌────────────────────────▼────────────────────────────────┐
│                   Repository Layer                      │
│  DepositRepository                                      │
└────────────────────────┬────────────────────────────────┘
                         │ DAO calls
┌────────────────────────▼────────────────────────────────┐
│                     Data Layer                          │
│  AppDatabase (Room Singleton)                           │
│  DepositDao                                             │
│  DepositCalculation (Entity)                            │
└─────────────────────────────────────────────────────────┘
```

### Навигационный граф

```
MainFragment
├──[Рассчитать]──► InputFragment (Step1 visible)
│                       └──[Далее]──► InputFragment (Step2 visible)
│                                          └──[Рассчитать]──► ResultFragment
│                                                                  └──[В начало]──► MainFragment
└──[История]──► HistoryFragment
                    └──[item click]──► DetailsFragment
                                           └──[Назад]──► HistoryFragment
```

Файл: `main/src/main/res/navigation/nav_graph.xml`

Destinations:
- `mainFragment` (startDestination)
- `inputFragment`
- `resultFragment`
- `historyFragment`
- `detailsFragment`

Actions:
- `mainFragment` → `inputFragment` (action_main_to_input)
- `mainFragment` → `historyFragment` (action_main_to_history)
- `inputFragment` → `resultFragment` (action_input_to_result), передаёт `calculationResult` (Parcelable или Safe Args)
- `historyFragment` → `detailsFragment` (action_history_to_details), передаёт `calculationId: Long`
- `detailsFragment` → `historyFragment` (popBackStack)

---

## Components and Interfaces

### Структура пакетов

```
ci.nsu.mobile.main/
├── ui/
│   ├── main/
│   │   ├── MainFragment.kt
│   │   └── MainViewModel.kt
│   ├── input/
│   │   ├── InputFragment.kt
│   │   └── InputViewModel.kt
│   ├── result/
│   │   ├── ResultFragment.kt
│   │   └── ResultViewModel.kt
│   ├── history/
│   │   ├── HistoryFragment.kt
│   │   ├── HistoryViewModel.kt
│   │   └── HistoryAdapter.kt
│   └── details/
│       ├── DetailsFragment.kt
│       └── DetailsViewModel.kt
├── data/
│   ├── db/
│   │   └── AppDatabase.kt
│   ├── dao/
│   │   └── DepositDao.kt
│   ├── entity/
│   │   └── DepositCalculation.kt
│   └── repository/
│       └── DepositRepository.kt
└── util/
    ├── DepositCalculator.kt
    └── Validator.kt
```

### UI компоненты

#### MainActivity
- Единственная Activity приложения
- Хостит `NavHostFragment`
- Не содержит бизнес-логики

#### MainFragment
- Отображает три кнопки: «Рассчитать», «История расчётов», «Закрыть приложение»
- Кнопка «Закрыть» вызывает `requireActivity().finish()`
- Навигация через `NavController`

#### InputFragment
- Содержит два «шага» в одном layout, переключаемых через `View.VISIBLE` / `View.GONE`
- **Step 1**: поля `InitialAmount`, `PeriodMonths`; кнопки «В начало», «Далее»
- **Step 2**: Spinner `InterestRate`, поле `MonthlyTopUp`; кнопки «Назад», «Рассчитать»
- Кнопка «Назад» на Step 2 переключает видимость обратно на Step 1 (не навигирует назад)
- Системная кнопка Back на Step 2 также переключает на Step 1 (через `OnBackPressedCallback`)
- Использует `InputViewModel` для хранения состояния

#### InputViewModel
- `MutableLiveData<String>` для `initialAmount`, `periodMonths`, `interestRate`, `monthlyTopUp`
- Метод `selectRateForPeriod(months: Int): Double` — возвращает ставку по правилу
- Метод `validateStep1(): ValidationResult`
- Метод `validateStep2(): ValidationResult`
- Метод `buildCalculationInput(): CalculationInput`

#### ResultFragment
- Отображает карточку с результатами
- Кнопка «Сохранить» вызывает `ResultViewModel.saveCalculation()`
- Показывает Snackbar «Расчёт сохранён» после успешного сохранения
- Кнопка «В начало» навигирует на `mainFragment` (popUpTo mainFragment inclusive=false)

#### ResultViewModel (AndroidViewModel)
- Принимает `CalculationResult` через `SavedStateHandle` или аргументы навигации
- `fun saveCalculation()` — вызывает `repository.insert(...)` в `viewModelScope`
- `LiveData<SaveState>` для отображения статуса сохранения

#### HistoryFragment
- `RecyclerView` со списком расчётов
- Пустое состояние: `TextView` «История расчётов пуста» (видим когда список пуст)
- Клик по элементу → навигация на `detailsFragment` с `calculationId`

#### HistoryViewModel (AndroidViewModel)
- `val calculations: LiveData<List<DepositCalculation>>` из `repository.getAllCalculations()`
- Автоматически обновляется при изменении БД (Room + LiveData)

#### HistoryAdapter
- `RecyclerView.Adapter` с `DiffUtil.ItemCallback<DepositCalculation>`
- Отображает: дату (форматированную), `initialAmount`, `finalAmount`
- `onItemClick: (Long) -> Unit` — callback для навигации

#### DetailsFragment
- Отображает все поля `DepositCalculation`
- Кнопка «Назад» → `findNavController().popBackStack()`

#### DetailsViewModel (AndroidViewModel)
- Принимает `calculationId: Long` через `SavedStateHandle`
- `val calculation: LiveData<DepositCalculation?>` из `repository.getById(id)`

### Утилиты

#### DepositCalculator

```kotlin
object DepositCalculator {
    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,   // в процентах, например 15.0
        monthlyTopUp: Double
    ): CalculationResult

    fun selectRate(periodMonths: Int): Double
}
```

Формула:
```
monthlyRate = interestRate / 100 / 12
FinalAmount = InitialAmount * (1 + monthlyRate)^PeriodMonths
            + MonthlyTopUp * ((1 + monthlyRate)^PeriodMonths - 1) / monthlyRate
InterestEarned = FinalAmount - (InitialAmount + MonthlyTopUp * PeriodMonths)
```

Особый случай: если `monthlyRate == 0.0` (ставка 0%), то `FinalAmount = InitialAmount + MonthlyTopUp * PeriodMonths`.

Округление: `BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toDouble()`

#### Validator

```kotlin
object Validator {
    fun validateInitialAmount(value: String): ValidationResult
    fun validatePeriodMonths(value: String): ValidationResult
    fun validateMonthlyTopUp(value: String): ValidationResult  // пустая строка — допустима (= 0)
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()
}
```

Правила валидации:
- `InitialAmount`: не пустое, парсится как Double, > 0
- `PeriodMonths`: не пустое, парсится как Int, > 0
- `MonthlyTopUp`: если не пустое — парсится как Double, ≥ 0

---

## Data Models

### DepositCalculation (Room Entity)

```kotlin
@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long   // System.currentTimeMillis()
)
```

### CalculationInput (in-memory, не хранится в БД)

```kotlin
data class CalculationInput(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double
)
```

### CalculationResult (in-memory, передаётся между фрагментами)

```kotlin
data class CalculationResult(
    val input: CalculationInput,
    val finalAmount: Double,
    val interestEarned: Double
) : Parcelable
```

### DepositDao

```kotlin
@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation): Long

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): LiveData<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    fun getById(id: Long): LiveData<DepositCalculation?>
}
```

### AppDatabase

```kotlin
@Database(entities = [DepositCalculation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposit_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
```

### DepositRepository

```kotlin
class DepositRepository(private val dao: DepositDao) {
    suspend fun insert(calculation: DepositCalculation) = dao.insert(calculation)
    fun getAllCalculations(): LiveData<List<DepositCalculation>> = dao.getAllCalculations()
    fun getById(id: Long): LiveData<DepositCalculation?> = dao.getById(id)
}
```

### Layouts

| Файл | Описание |
|---|---|
| `activity_main.xml` | `FragmentContainerView` для NavHostFragment |
| `fragment_input.xml` | Два блока (Step1 / Step2) с переключением visibility |
| `fragment_result.xml` | `CardView` с полями результата, кнопки «Сохранить» и «В начало» |
| `fragment_history.xml` | `RecyclerView` + `TextView` для пустого состояния |
| `item_history.xml` | Строка списка: дата, InitialAmount, FinalAmount |
| `fragment_details.xml` | Все поля `DepositCalculation` |

### Зависимости (добавить в `main/build.gradle.kts`)

```kotlin
// Navigation Component
implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// ViewModel + LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

// Material Design
implementation("com.google.android.material:material:1.11.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("io.kotest:kotest-property:5.8.1")
testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
```

Также добавить плагин `kotlin-kapt` и `useJUnitPlatform()` в тестовую конфигурацию.


---

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system — essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

Для тестирования свойств используется библиотека **Kotest Property Testing** (`io.kotest:kotest-property:5.8.1`). Каждый тест запускается минимум **100 итераций** со случайно сгенерированными входными данными.

---

### Property 1: Validator отклоняет невалидные входные данные

*For any* строки, представляющей пустое значение, ноль, отрицательное число или непарсируемый текст:
- `Validator.validateInitialAmount` должен вернуть `ValidationResult.Invalid`
- `Validator.validatePeriodMonths` должен вернуть `ValidationResult.Invalid`
- `Validator.validateMonthlyTopUp` должен вернуть `ValidationResult.Invalid` для любого отрицательного числа

*For any* строки, представляющей положительное число (> 0):
- `Validator.validateInitialAmount` и `Validator.validatePeriodMonths` должны вернуть `ValidationResult.Valid`

*For any* строки, представляющей неотрицательное число (≥ 0), или пустой строки:
- `Validator.validateMonthlyTopUp` должен вернуть `ValidationResult.Valid`

**Validates: Requirements 2.5, 2.6, 3.7**

---

### Property 2: Правило выбора процентной ставки

*For any* положительного целого числа `periodMonths`:
- Если `periodMonths < 6`, то `DepositCalculator.selectRate(periodMonths)` должен вернуть `15.0`
- Если `6 ≤ periodMonths < 12`, то `DepositCalculator.selectRate(periodMonths)` должен вернуть `10.0`
- Если `periodMonths ≥ 12`, то `DepositCalculator.selectRate(periodMonths)` должен вернуть `5.0`

**Validates: Requirements 3.1**

---

### Property 3: Корректность формулы расчёта

*For any* валидных входных данных (`initialAmount > 0`, `periodMonths > 0`, `interestRate > 0`, `monthlyTopUp ≥ 0`):

Пусть `monthlyRate = interestRate / 100 / 12`, тогда результат `DepositCalculator.calculate(...)` должен удовлетворять:

```
finalAmount ≈ initialAmount * (1 + monthlyRate)^periodMonths
            + monthlyTopUp * ((1 + monthlyRate)^periodMonths - 1) / monthlyRate
```

(с допуском ±0.01 на округление)

**Validates: Requirements 4.1, 4.2**

---

### Property 4: Инварианты выходных данных Calculator

*For any* валидных входных данных:

1. **Инвариант InterestEarned**: `interestEarned == finalAmount - (initialAmount + monthlyTopUp * periodMonths)` (с допуском ±0.01)
2. **Инвариант округления**: `finalAmount` и `interestEarned` имеют не более двух знаков после запятой

**Validates: Requirements 4.3, 4.4**

---

### Property 5: Round-trip сохранения расчёта

*For any* валидного объекта `DepositCalculation` (со случайными значениями всех полей):
- После вызова `DepositDao.insert(calculation)` и последующего `DepositDao.getById(id)` — полученный объект должен содержать те же значения всех полей: `initialAmount`, `periodMonths`, `interestRate`, `monthlyTopUp`, `finalAmount`, `interestEarned`, `calculationDate`

**Validates: Requirements 5.3, 8.4**

---

### Property 6: История отсортирована по дате убывания и реактивна

*For any* набора из N объектов `DepositCalculation` со случайными значениями `calculationDate`:
- После вставки всех записей в БД, `DepositDao.getAllCalculations()` должен вернуть список, в котором для каждой пары соседних элементов `list[i].calculationDate ≥ list[i+1].calculationDate`
- После вставки новой записи, `LiveData` должна эмитировать обновлённый список, содержащий новую запись

**Validates: Requirements 6.1, 6.5, 8.2**

---

### Property 7: HistoryAdapter отображает обязательные поля

*For any* объекта `DepositCalculation`:
- Отрендеренный элемент списка (через `HistoryAdapter`) должен содержать строковое представление `calculationDate` (форматированную дату), `initialAmount` и `finalAmount`

**Validates: Requirements 6.2**

---

### Property 8: DetailsFragment отображает все поля расчёта

*For any* объекта `DepositCalculation`:
- `DetailsFragment`, получив данный расчёт, должен отображать все поля: дату, `initialAmount`, `periodMonths`, `interestRate`, `monthlyTopUp`, `finalAmount`, `interestEarned`

**Validates: Requirements 7.1**

---

## Error Handling

### Валидация ввода

- Ошибки валидации отображаются через `TextInputLayout.error` непосредственно под соответствующим полем
- Переход на следующий шаг блокируется до устранения всех ошибок
- Ошибка сбрасывается при изменении текста в поле (`addTextChangedListener`)

### Ошибки базы данных

- Операции с БД выполняются в `viewModelScope` с `Dispatchers.IO`
- При ошибке сохранения `ResultViewModel` эмитирует `SaveState.Error`, фрагмент показывает Snackbar с сообщением об ошибке
- Room не бросает исключений при корректном использовании; критические ошибки (нехватка места) логируются

### Граничные случаи Calculator

- `monthlyTopUp = 0.0`: формула корректно вычисляется (второй член равен нулю)
- `monthlyRate = 0.0` (ставка 0%): специальный случай, `FinalAmount = InitialAmount + MonthlyTopUp * PeriodMonths`
- Очень большие значения: использование `Double` достаточно для банковских расчётов в разумных диапазонах; округление через `BigDecimal` предотвращает накопление ошибок

### Навигация

- Кнопка Back на Step 2 перехватывается через `OnBackPressedCallback` и переключает видимость на Step 1 вместо навигации назад
- `popBackStack()` используется для возврата с DetailsFragment на HistoryFragment

---

## Testing Strategy

### Подход

Используется двойная стратегия тестирования:
- **Unit-тесты** (JVM, без Android): чистая логика `DepositCalculator` и `Validator`
- **Property-based тесты** (Kotest, JVM): универсальные свойства Calculator и Validator
- **Instrumented тесты** (Android): Room DAO, ViewModel, навигация (Espresso)

### Property-Based Testing

Библиотека: **Kotest Property Testing** (`io.kotest:kotest-property:5.8.1`)  
Минимальное количество итераций: **100** на каждый тест  
Тег каждого теста: `Feature: deposit-calculator, Property N: <текст свойства>`

Генераторы:
- `Arb.double(min = 0.01, max = 1_000_000.0)` — для `initialAmount`, `monthlyTopUp`
- `Arb.int(min = 1, max = 360)` — для `periodMonths`
- `Arb.element(5.0, 10.0, 15.0)` — для `interestRate`
- `Arb.string()` с фильтрами — для тестов Validator
- `Arb.long(min = 0)` — для `calculationDate`

Каждое свойство из раздела Correctness Properties реализуется **одним** property-based тестом.

### Unit-тесты

Фокус:
- Конкретные примеры расчёта (известные входные данные → ожидаемый результат)
- Граничные случаи: `monthlyTopUp = 0`, `periodMonths = 1`, `periodMonths = 6`, `periodMonths = 12`
- Валидация: конкретные невалидные строки ("", "0", "-1", "abc")

### Instrumented тесты (Espresso + Room)

- DAO тесты: используют `Room.inMemoryDatabaseBuilder` для изоляции
- ViewModel тесты: используют `InstantTaskExecutorRule` для синхронного выполнения LiveData
- UI тесты: Espresso для навигации и отображения данных
- Smoke тесты: проверка Singleton AppDatabase, выживаемость ViewModel при повороте экрана

### Покрытие по требованиям

| Требование | Тип теста | Property # |
|---|---|---|
| 2.5, 2.6, 3.7 | Property-based | Property 1 |
| 3.1 | Property-based | Property 2 |
| 4.1, 4.2 | Property-based | Property 3 |
| 4.3, 4.4 | Property-based | Property 4 |
| 5.3, 8.4 | Property-based | Property 5 |
| 6.1, 6.5, 8.2 | Property-based | Property 6 |
| 6.2 | Property-based | Property 7 |
| 7.1 | Property-based | Property 8 |
| 1.x, 2.1–2.4, 3.2–3.5, 5.1–5.2, 5.4–5.5, 6.3–6.4, 7.2 | Unit / Espresso | — |
| 2.7, 3.6, 5.6, 7.3, 8.1, 8.3 | Smoke / Instrumented | — |
| 9.x | Code review | — |

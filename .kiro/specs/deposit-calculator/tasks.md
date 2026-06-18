# Implementation Plan: Расчёт вкладов

## Overview

Реализация Android-приложения «Расчёт вкладов» на Kotlin с архитектурой MVVM, Navigation Component, Room и LiveData. Задачи выстроены инкрементально: сначала фундамент (зависимости, структура, утилиты), затем слои данных, ViewModel и UI, в конце — интеграция и тесты.

## Tasks

- [x] 1. Настройка зависимостей и плагинов сборки
  - Добавить плагины `kotlin-kapt` и `androidx.navigation.safeargs.kotlin` в `main/build.gradle.kts` (блок `plugins`)
  - Добавить плагин `androidx.navigation.safeargs.kotlin` в корневой `build.gradle.kts` (блок `plugins`, `apply false`)
  - Добавить в `main/build.gradle.kts` зависимости:
    - Navigation Component: `navigation-fragment-ktx:2.7.7`, `navigation-ui-ktx:2.7.7`
    - Room: `room-runtime:2.6.1`, `room-ktx:2.6.1`, `kapt("room-compiler:2.6.1")`
    - Lifecycle: `lifecycle-viewmodel-ktx:2.7.0`, `lifecycle-livedata-ktx:2.7.0`
    - Material Design: `com.google.android.material:material:1.11.0`
    - Coroutines: `kotlinx-coroutines-android:1.7.3`
    - Тесты: `io.kotest:kotest-property:5.8.1`, `io.kotest:kotest-runner-junit5:5.8.1`
  - Включить `ViewBinding` в блоке `android { buildFeatures { viewBinding = true } }`
  - Добавить `useJUnitPlatform()` в блок `tasks.withType<Test>`
  - _Requirements: 9.4_

- [x] 2. Утилиты: DepositCalculator и Validator
  - [x] 2.1 Создать `main/src/main/java/ci/nsu/mobile/main/util/DepositCalculator.kt`
    - Реализовать `object DepositCalculator` с методами `calculate(...)` и `selectRate(periodMonths: Int): Double`
    - Формула сложных процентов с ежемесячным пополнением; особый случай `monthlyRate == 0.0`
    - Округление через `BigDecimal.setScale(2, RoundingMode.HALF_UP)`
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 3.1_

  - [ ]* 2.2 Написать property-тест для правила выбора ставки (Property 2)
    - **Property 2: Правило выбора процентной ставки**
    - Для любого `periodMonths > 0`: `< 6 → 15.0`, `6..11 → 10.0`, `≥ 12 → 5.0`
    - Файл: `main/src/test/java/ci/nsu/mobile/main/util/DepositCalculatorPropertyTest.kt`
    - **Validates: Requirements 3.1**

  - [ ]* 2.3 Написать property-тест для формулы расчёта (Property 3)
    - **Property 3: Корректность формулы расчёта сложных процентов**
    - Для любых валидных входных данных результат совпадает с эталонной формулой (допуск ±0.01)
    - Файл: `main/src/test/java/ci/nsu/mobile/main/util/DepositCalculatorPropertyTest.kt`
    - **Validates: Requirements 4.1, 4.2**

  - [ ]* 2.4 Написать property-тест для инвариантов выходных данных (Property 4)
    - **Property 4: Инварианты выходных данных Calculator**
    - `interestEarned == finalAmount - (initialAmount + monthlyTopUp * periodMonths)` (±0.01)
    - `finalAmount` и `interestEarned` имеют не более двух знаков после запятой
    - Файл: `main/src/test/java/ci/nsu/mobile/main/util/DepositCalculatorPropertyTest.kt`
    - **Validates: Requirements 4.3, 4.4**

  - [x] 2.5 Создать `main/src/main/java/ci/nsu/mobile/main/util/Validator.kt`
    - Реализовать `object Validator` с методами `validateInitialAmount`, `validatePeriodMonths`, `validateMonthlyTopUp`
    - Реализовать `sealed class ValidationResult` (`Valid`, `Invalid(errorMessage: String)`)
    - Правила: `InitialAmount` — не пустое, Double, > 0; `PeriodMonths` — не пустое, Int, > 0; `MonthlyTopUp` — пустая строка допустима (= 0), иначе Double, ≥ 0
    - _Requirements: 2.5, 2.6, 3.7_

  - [ ]* 2.6 Написать property-тест для Validator (Property 1)
    - **Property 1: Validator отклоняет невалидные входные данные**
    - Для пустых, нулевых, отрицательных и непарсируемых строк — `Invalid`; для валидных — `Valid`
    - Файл: `main/src/test/java/ci/nsu/mobile/main/util/ValidatorPropertyTest.kt`
    - **Validates: Requirements 2.5, 2.6, 3.7**

- [x] 3. Checkpoint — проверка утилит
  - Убедиться, что все тесты из задачи 2 проходят. Уточнить у пользователя, если возникнут вопросы.

- [x] 4. Слой данных: Entity, DAO, Database, Repository
  - [x] 4.1 Создать `main/src/main/java/ci/nsu/mobile/main/data/entity/DepositCalculation.kt`
    - `@Entity(tableName = "deposit_calculations")` data class со всеми полями: `id`, `initialAmount`, `periodMonths`, `interestRate`, `monthlyTopUp`, `finalAmount`, `interestEarned`, `calculationDate`
    - `@PrimaryKey(autoGenerate = true) val id: Long = 0`
    - _Requirements: 8.4_

  - [x] 4.2 Создать `main/src/main/java/ci/nsu/mobile/main/data/dao/DepositDao.kt`
    - `@Dao interface DepositDao` с методами: `@Insert suspend fun insert(...)`, `@Query getAllCalculations(): LiveData<List<DepositCalculation>>` (ORDER BY calculationDate DESC), `@Query getById(id: Long): LiveData<DepositCalculation?>`
    - _Requirements: 8.2_

  - [x] 4.3 Создать `main/src/main/java/ci/nsu/mobile/main/data/db/AppDatabase.kt`
    - `@Database(entities = [DepositCalculation::class], version = 1)` abstract class
    - Singleton через `companion object` с `@Volatile INSTANCE` и `synchronized` блоком
    - _Requirements: 8.1_

  - [x] 4.4 Создать `main/src/main/java/ci/nsu/mobile/main/data/repository/DepositRepository.kt`
    - `class DepositRepository(private val dao: DepositDao)` с методами `insert`, `getAllCalculations`, `getById`
    - Все suspend-операции делегируются DAO; `insert` вызывается из coroutine с `Dispatchers.IO`
    - _Requirements: 8.3_

  - [ ]* 4.5 Написать instrumented property-тест round-trip сохранения (Property 5)
    - **Property 5: Round-trip сохранения расчёта в Room**
    - Использовать `Room.inMemoryDatabaseBuilder`; для любого случайного `DepositCalculation` — insert → getById → поля совпадают
    - Файл: `main/src/androidTest/java/ci/nsu/mobile/main/data/dao/DepositDaoPropertyTest.kt`
    - **Validates: Requirements 5.3, 8.4**

  - [ ]* 4.6 Написать instrumented property-тест сортировки истории (Property 6)
    - **Property 6: История отсортирована по дате убывания**
    - Для N случайных записей: `getAllCalculations()` возвращает список с `list[i].calculationDate ≥ list[i+1].calculationDate`; после вставки новой записи LiveData эмитирует обновлённый список
    - Файл: `main/src/androidTest/java/ci/nsu/mobile/main/data/dao/DepositDaoPropertyTest.kt`
    - **Validates: Requirements 6.1, 6.5, 8.2**

- [x] 5. Checkpoint — проверка слоя данных
  - Убедиться, что instrumented тесты из задачи 4 проходят. Уточнить у пользователя, если возникнут вопросы.

- [x] 6. Модели данных и навигационный граф
  - [x] 6.1 Создать data-классы `CalculationInput` и `CalculationResult` (Parcelable)
    - `CalculationInput(initialAmount, periodMonths, interestRate, monthlyTopUp)` — in-memory, не хранится в БД
    - `CalculationResult(input: CalculationInput, finalAmount, interestEarned) : Parcelable` — передаётся между фрагментами через Safe Args
    - Файл: `main/src/main/java/ci/nsu/mobile/main/data/entity/CalculationModels.kt`
    - _Requirements: 4.1, 5.1_

  - [x] 6.2 Создать `main/src/main/res/navigation/nav_graph.xml`
    - Destinations: `mainFragment` (startDestination), `inputFragment`, `resultFragment`, `historyFragment`, `detailsFragment`
    - Actions: `action_main_to_input`, `action_main_to_history`, `action_input_to_result` (аргумент `calculationResult: CalculationResult`), `action_history_to_details` (аргумент `calculationId: Long`)
    - _Requirements: 9.4_

- [x] 7. Layout-файлы
  - [x] 7.1 Создать `main/src/main/res/layout/activity_main.xml`
    - `FragmentContainerView` с `app:navGraph="@navigation/nav_graph"` и `app:defaultNavHost="true"`
    - _Requirements: 9.4_

  - [x] 7.2 Создать `main/src/main/res/layout/fragment_main.xml`
    - Три кнопки `MaterialButton`: «Рассчитать», «История расчётов», «Закрыть приложение»
    - _Requirements: 1.2, 1.3, 1.4_

  - [x] 7.3 Создать `main/src/main/res/layout/fragment_input.xml`
    - Блок Step1: `TextInputLayout`/`TextInputEditText` для `InitialAmount` и `PeriodMonths`; кнопки «В начало», «Далее»
    - Блок Step2 (изначально `GONE`): `Spinner` для `InterestRate`; `TextInputLayout`/`TextInputEditText` для `MonthlyTopUp`; кнопки «Назад», «Рассчитать»
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 3.1, 3.2, 3.3, 3.4, 3.5_

  - [x] 7.4 Создать `main/src/main/res/layout/fragment_result.xml`
    - `CardView` с полями: `InitialAmount`, `PeriodMonths`, `InterestRate`, `MonthlyTopUp`, `FinalAmount`, `InterestEarned`
    - Кнопки «Сохранить» и «В начало»
    - _Requirements: 5.1, 5.2, 5.3, 5.5_

  - [x] 7.5 Создать `main/src/main/res/layout/fragment_history.xml`
    - `RecyclerView` для списка расчётов
    - `TextView` «История расчётов пуста» (видимость управляется из ViewModel)
    - _Requirements: 6.1, 6.4_

  - [x] 7.6 Создать `main/src/main/res/layout/item_history.xml`
    - Строка списка: дата расчёта, `InitialAmount`, `FinalAmount`
    - _Requirements: 6.2_

  - [x] 7.7 Создать `main/src/main/res/layout/fragment_details.xml`
    - Все поля `DepositCalculation`: дата, `InitialAmount`, `PeriodMonths`, `InterestRate`, `MonthlyTopUp`, `FinalAmount`, `InterestEarned`
    - _Requirements: 7.1_

- [x] 8. ViewModel-слой
  - [x] 8.1 Создать `main/src/main/java/ci/nsu/mobile/main/ui/main/MainViewModel.kt`
    - Пустой `ViewModel` (навигация управляется из фрагмента через `NavController`)
    - _Requirements: 9.1_

  - [x] 8.2 Создать `main/src/main/java/ci/nsu/mobile/main/ui/input/InputViewModel.kt`
    - `MutableLiveData<String>` для `initialAmount`, `periodMonths`, `interestRate`, `monthlyTopUp`
    - Метод `selectRateForPeriod(months: Int): Double` — делегирует `DepositCalculator.selectRate`
    - Методы `validateStep1(): ValidationResult` и `validateStep2(): ValidationResult`
    - Метод `buildCalculationInput(): CalculationInput`
    - _Requirements: 2.7, 3.6, 9.1, 9.3_

  - [x] 8.3 Создать `main/src/main/java/ci/nsu/mobile/main/ui/result/ResultViewModel.kt`
    - `AndroidViewModel`; принимает `CalculationResult` через `SavedStateHandle`
    - `fun saveCalculation()` — вызывает `repository.insert(...)` в `viewModelScope` с `Dispatchers.IO`
    - `LiveData<SaveState>` (sealed class: `Idle`, `Success`, `Error`) для статуса сохранения
    - _Requirements: 5.3, 5.4, 5.6, 9.1, 9.3_

  - [x] 8.4 Создать `main/src/main/java/ci/nsu/mobile/main/ui/history/HistoryViewModel.kt`
    - `AndroidViewModel`; `val calculations: LiveData<List<DepositCalculation>>` из `repository.getAllCalculations()`
    - _Requirements: 6.5, 9.1_

  - [x] 8.5 Создать `main/src/main/java/ci/nsu/mobile/main/ui/details/DetailsViewModel.kt`
    - `AndroidViewModel`; принимает `calculationId: Long` через `SavedStateHandle`
    - `val calculation: LiveData<DepositCalculation?>` из `repository.getById(id)`
    - _Requirements: 7.3, 9.1_

- [x] 9. UI-слой: Activity, Fragments, Adapter
  - [x] 9.1 Создать `main/src/main/java/ci/nsu/mobile/main/ui/main/MainActivity.kt`
    - Единственная Activity; хостит `NavHostFragment`; обновить `AndroidManifest.xml`
    - _Requirements: 9.4_

  - [x] 9.2 Создать `main/src/main/java/ci/nsu/mobile/main/ui/main/MainFragment.kt`
    - ViewBinding; кнопка «Рассчитать» → `navigate(action_main_to_input)`; кнопка «История» → `navigate(action_main_to_history)`; кнопка «Закрыть» → `requireActivity().finish()`
    - _Requirements: 1.1, 1.2, 1.3, 1.4_

  - [x] 9.3 Создать `main/src/main/java/ci/nsu/mobile/main/ui/input/InputFragment.kt`
    - ViewBinding; переключение видимости Step1/Step2 через `View.VISIBLE`/`View.GONE`
    - Step1: валидация через `InputViewModel.validateStep1()`, ошибки через `TextInputLayout.error`; кнопка «Далее» переключает на Step2 и вызывает `selectRateForPeriod`
    - Step2: Spinner с тремя значениями ставки; `OnBackPressedCallback` для возврата на Step1; кнопка «Рассчитать» → валидация → `DepositCalculator.calculate(...)` → `navigate(action_input_to_result, calculationResult)`
    - _Requirements: 2.1–2.7, 3.1–3.7_

  - [x] 9.4 Создать `main/src/main/java/ci/nsu/mobile/main/ui/result/ResultFragment.kt`
    - ViewBinding; отображение всех полей `CalculationResult`; кнопка «Сохранить» → `ResultViewModel.saveCalculation()`; наблюдение за `SaveState` → Snackbar «Расчёт сохранён» или сообщение об ошибке; кнопка «В начало» → `navigate` с `popUpTo(mainFragment)`
    - _Requirements: 5.1–5.6_

  - [x] 9.5 Создать `main/src/main/java/ci/nsu/mobile/main/ui/history/HistoryAdapter.kt`
    - `RecyclerView.Adapter` с `DiffUtil.ItemCallback<DepositCalculation>`
    - Отображает дату (форматированную через `SimpleDateFormat`), `initialAmount`, `finalAmount`
    - `onItemClick: (Long) -> Unit` callback
    - _Requirements: 6.2, 6.3_

  - [ ]* 9.6 Написать instrumented property-тест для HistoryAdapter (Property 7)
    - **Property 7: HistoryAdapter отображает обязательные поля**
    - Для любого `DepositCalculation` отрендеренный элемент содержит строковое представление даты, `initialAmount` и `finalAmount`
    - Файл: `main/src/androidTest/java/ci/nsu/mobile/main/ui/history/HistoryAdapterPropertyTest.kt`
    - **Validates: Requirements 6.2**

  - [x] 9.7 Создать `main/src/main/java/ci/nsu/mobile/main/ui/history/HistoryFragment.kt`
    - ViewBinding; `RecyclerView` с `HistoryAdapter`; наблюдение за `HistoryViewModel.calculations` → переключение видимости пустого состояния; клик по элементу → `navigate(action_history_to_details, calculationId)`
    - _Requirements: 6.1, 6.3, 6.4, 6.5_

  - [x] 9.8 Создать `main/src/main/java/ci/nsu/mobile/main/ui/details/DetailsFragment.kt`
    - ViewBinding; наблюдение за `DetailsViewModel.calculation` → отображение всех полей; кнопка «Назад» → `findNavController().popBackStack()`
    - _Requirements: 7.1, 7.2, 7.3_

  - [ ]* 9.9 Написать instrumented property-тест для DetailsFragment (Property 8)
    - **Property 8: DetailsFragment отображает все поля расчёта**
    - Для любого `DepositCalculation` фрагмент отображает все поля: дату, `initialAmount`, `periodMonths`, `interestRate`, `monthlyTopUp`, `finalAmount`, `interestEarned`
    - Файл: `main/src/androidTest/java/ci/nsu/mobile/main/ui/details/DetailsFragmentPropertyTest.kt`
    - **Validates: Requirements 7.1**

- [x] 10. Интеграция и финальная сборка
  - [x] 10.1 Обновить `AndroidManifest.xml`
    - Добавить `<activity android:name=".ui.main.MainActivity">` как launcher activity
    - _Requirements: 9.4_

  - [x] 10.2 Связать все компоненты
    - Убедиться, что `AppDatabase.getInstance(context)` вызывается корректно из `HistoryViewModel`, `ResultViewModel`, `DetailsViewModel`
    - Проверить, что Safe Args генерирует корректные классы для `CalculationResult` и `calculationId`
    - Проверить, что `OnBackPressedCallback` в `InputFragment` корректно перехватывает системную кнопку Back на Step2
    - _Requirements: 8.1, 9.2, 9.4_

  - [ ]* 10.3 Написать unit-тесты для граничных случаев Calculator
    - Конкретные примеры: `monthlyTopUp = 0`, `periodMonths = 1`, `periodMonths = 6`, `periodMonths = 12`
    - Файл: `main/src/test/java/ci/nsu/mobile/main/util/DepositCalculatorUnitTest.kt`
    - _Requirements: 4.1, 4.2, 4.3, 4.4_

  - [ ]* 10.4 Написать unit-тесты для Validator с конкретными значениями
    - Невалидные строки: `""`, `"0"`, `"-1"`, `"abc"`, `"-0.5"`; валидные: `"100"`, `"1"`, `"0"` (для MonthlyTopUp)
    - Файл: `main/src/test/java/ci/nsu/mobile/main/util/ValidatorUnitTest.kt`
    - _Requirements: 2.5, 2.6, 3.7_

- [x] 11. Финальный checkpoint
  - Убедиться, что все обязательные тесты проходят, приложение собирается без ошибок. Уточнить у пользователя, если возникнут вопросы.

## Notes

- Задачи, помеченные `*`, являются опциональными и могут быть пропущены для ускорения MVP
- Property-based тесты (Properties 1–4) запускаются на JVM (`main/src/test/`); Properties 5–8 — instrumented (`main/src/androidTest/`)
- Каждый property-тест использует минимум 100 итераций Kotest
- Safe Args требует, чтобы `CalculationResult` реализовывал `Parcelable` (или `@Parcelize`)
- `InputFragment` использует один layout с двумя блоками и переключением `visibility` — не два отдельных фрагмента
- Все операции с Room выполняются через `viewModelScope` + `Dispatchers.IO`
- `HistoryViewModel` и `DetailsViewModel` — `AndroidViewModel` (нужен `Application` для `AppDatabase.getInstance`)

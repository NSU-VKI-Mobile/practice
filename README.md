# Отчет по лабораторной работе №4

**Тема:** Разработка UI с использованием паттерна MVVM и подхода UiState в Jetpack Compose
**Вариант:** 1 (Простой счетчик с историей)

---

## 1. Цель работы
Изучить и применить на практике архитектурный паттерн MVVM (Model-View-ViewModel) совместно с подходом единого состояния экрана (`UiState`). Освоить реактивное обновление пользовательского интерфейса с помощью декларативного фреймворка Jetpack Compose и `StateFlow`.

## 2. Задание
Создать Android-приложение «Счетчик», удовлетворяющее следующим требованиям:
* Отображение текущего значения счетчика.
* Наличие трех кнопок управления: «+», «-», «Сброс».
* Отображение истории последних 5 действий.
* Использование `StateFlow` для управления состоянием UI.
* Обеспечение сохранения состояния при смене конфигурации (поворот экрана).
* Отсутствие прямого изменения UI из `ViewModel`.

---

## 3. Ход выполнения работы

### 3.1. Подготовка структуры проекта и настройка сборок
В исходном репозитории отсутствовала базовая структура папок для исходного кода и не был активирован Jetpack Compose.
* Было воссоздано стандартное дерево директорий: `src/main/java/ci/nsu/mobile/main`.
* В конфигурационном файле `build.gradle.kts` модуля `main` был включен флаг `buildFeatures { compose = true }` и добавлены необходимые зависимости: `androidx.compose.ui`, `androidx.compose.material3`, а также `lifecycle-viewmodel-compose` для связывания ViewModel с Compose-функциями.

### 3.2. Описание состояния (UiState)
В соответствии с концепцией Unidirectional Data Flow (UDF), всё изменяемое состояние экрана было инкапсулировано в единый неизменяемый `data class`.

```kotlin
package ci.nsu.mobile.main

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)
```

### 3.3. Реализация бизнес-логики (ViewModel)
Был создан класс `CounterViewModel`, наследующийся от `androidx.lifecycle.ViewModel`. 
* Для хранения состояния использован `MutableStateFlow`, который наружу отдается как read-only `StateFlow`.
* Для изменения состояния реализованы методы `increment()`, `decrement()` и `reset()`. 
* Обновление состояния производится потокобезопасным методом `update`. 
* Логика хранения истории ограничена строго 5 последними элементами с помощью функции `take(4)` (сохранение 4 предыдущих записей + 1 новая в начало списка).

```kotlin
package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()
    
    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(count = newCount, history = newHistory)
        }
    }
    
    // Методы decrement() и reset() реализованы по аналогичному принципу
}
```

### 3.4. Декларативная верстка интерфейса (Jetpack Compose)
Создана Composable-функция `CounterScreen`, отвечающая за отрисовку интерфейса.
* Подписка на обновления состояния осуществляется через делегат `by viewModel.uiState.collectAsState()`. Это гарантирует автоматическую рекомпозицию (перерисовку) экрана при любом изменении данных во ViewModel.
* Кнопки управления напрямую не меняют данные, а лишь делегируют события в `ViewModel` (например, `onClick = { viewModel.increment() }`).
* Для вывода списка истории использован компонент `LazyColumn`, оптимизированный для работы со списками.

### 3.5. Интеграция в MainActivity
В точке входа приложения (`MainActivity`) компонент `CounterScreen` был помещен в блок `setContent`. Инстанцирование ViewModel происходит через вызов `viewModel()`, что позволяет системе Android сохранять объект ViewModel в памяти при уничтожении и пересоздании Activity во время поворота экрана.

---

## 4. Результаты и выводы
Критерии проверки успешно выполнены:
1. **Сборка проекта:** Приложение компилируется и запускается без ошибок.
2. **Архитектура:** Строго соблюден паттерн MVVM. Интерфейс ничего не знает о бизнес-логике, а `ViewModel` не имеет прямых ссылок на UI-компоненты.
3. **Реактивность:** Использование `StateFlow` обеспечивает мгновенный и безопасный отклик интерфейса на любые действия пользователя.
4. **Устойчивость к поворотам экрана:** Тестирование показало, что при смене ориентации экрана счетчик и история не сбрасываются, так как данные хранятся на уровне `ViewModel`.

Цель лабораторной работы полностью достигнута.

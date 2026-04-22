# Лабораторная работа: MVVM с UiState подходом

## Правила оформления
**Название ветки:** Группа_фамилия_номерЛабораторной_вариант

## Варианты заданий

### Вариант 1: Простой счетчик с историей
**Цель:** Понять базовую структуру ViewModel + UiState

**Задание:** Создать приложение "Счетчик", которое:
1. Показывает текущее значение счетчика
2. Имеет 3 кнопки: "+", "-", "Сброс"
3. Показывает историю последних 5 действий

**Код для ViewModel:**
```kotlin
// UiState - простой data class
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class CounterViewModel : ViewModel() {
    // StateFlow для UiState
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()
    
    // Методы для изменения состояния
    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }
    
    fun decrement() {
        // TODO: реализовать аналогично increment()
    }
    
    fun reset() {
        // TODO: реализовать
    }
}
```

**Реализовать:**
1. Создать экран с:
   - Text для отображения uiState.count
   - Column с 3 кнопками
   - LazyColumn для отображения uiState.history
2. Связать кнопки с методами ViewModel
3. Показать, что состояние сохраняется при повороте экрана

---
## Общий шаблон для всех лабораторных работ

### 1. Файл ViewModel:
```kotlin
data class MyUiState(
    // свойства состояния
)

class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()
    
    fun updateSomething(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(/* изменения */)
        }
    }
}
```

### 2. Файл Composable (UI):
```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Column {
        // Отображение uiState
        Text(text = uiState.someProperty)
        
        // Вызов методов ViewModel
        Button(onClick = { viewModel.doSomething() }) {
            Text("Кнопка")
        }
    }
}
```

### 3. MainActivity:
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                MyScreen()
            }
        }
    }
}
```

---

## Критерии проверки:
1. Приложение собирается без ошибок
2. Используется StateFlow для UiState
3. UI реагирует на изменения состояния
4. Состояние сохраняется при повороте экрана
5. Нет прямого изменения UI из ViewModel

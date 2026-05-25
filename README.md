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

### Вариант 2: Конвертер температуры
**Цель:** Работа с вычисляемыми значениями в UiState

**Задание:** Конвертер между Цельсием и Фаренгейтом
1. Поле ввода для градусов Цельсия
2. Автоматический расчет Фаренгейта
3. Поле ввода для градусов Фаренгейта
4. Автоматический расчет Цельсия

**Код для ViewModel:**
```kotlin
data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    // Вычисляемые свойства для валидации
    val isCelsiusValid: Boolean get() = celsius.toDoubleOrNull() != null
    val isFahrenheitValid: Boolean get() = fahrenheit.toDoubleOrNull() != null
}

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()
    
    fun onCelsiusChanged(newValue: String) {
        _uiState.update { currentState ->
            val celsius = newValue
            val fahrenheit = if (celsius.isNotBlank()) {
                val c = celsius.toDoubleOrNull()
                if (c != null) String.format("%.2f", c * 9/5 + 32)
                else ""
            } else ""
            
            currentState.copy(
                celsius = celsius,
                fahrenheit = fahrenheit
            )
        }
    }
    
    fun onFahrenheitChanged(newValue: String) {
        // TODO: реализовать обратный расчет
    }
}
```

**Реализовать:**
1. Создать 2 TextField
2. Связать их значения с uiState.celsius и uiState.fahrenheit
3. Использовать isCelsiusValid для подсветки ошибок
4. Показать автоматический пересчет при вводе

---

### Вариант 3: Список покупок (CRUD без БД)
**Цель:** Работа со списками в UiState

**Задание:** Простой список покупок
1. Поле ввода + кнопка "Добавить"
2. Список добавленных товаров
3. Возможность отметить товар купленным
4. Возможность удалить товар

**Код для ViewModel:**
```kotlin
data class ShoppingItem(
    val id: Int,
    val name: String,
    val isBought: Boolean = false
)

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val newItemText: String = ""
)

class ShoppingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()
    
    fun onNewItemTextChanged(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }
    
    fun addItem() {
        val currentText = _uiState.value.newItemText
        if (currentText.isNotBlank()) {
            _uiState.update { currentState ->
                val newItem = ShoppingItem(
                    id = currentState.items.size + 1,
                    name = currentText
                )
                currentState.copy(
                    items = currentState.items + newItem,
                    newItemText = ""
                )
            }
        }
    }
    
    fun toggleItemBought(itemId: Int) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.map { item ->
                if (item.id == itemId) {
                    item.copy(isBought = !item.isBought)
                } else {
                    item
                }
            }
            currentState.copy(items = updatedItems)
        }
    }
    
    fun deleteItem(itemId: Int) {
        // TODO: реализовать
    }
}
```

**Реализовать:**
1. Создать TextField и кнопку "Добавить"
2. LazyColumn для списка товаров
3. Каждый элемент: текст + Checkbox + кнопка удаления
4. Показать изменение состояния списка

---

### Вариант 4: Выбор цвета с предпросмотром
**Цель:** Работа с несколькими свойствами в UiState

**Задание:** Выбор цвета для текста
1. 3 ползунка (Slider) для RGB
2. Текст-предпросмотр с выбранным цветом
3. Кнопка "Случайный цвет"

**Код для ViewModel:**
```kotlin
data class ColorUiState(
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128
) {
    // Вычисляемый цвет
    val color: Color get() = Color(red, green, blue)
    
    // Текстовое представление
    val hexCode: String get() = "#${red.toString(16)}${green.toString(16)}${blue.toString(16)}"
}

class ColorPickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ColorUiState())
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()
    
    fun onRedChanged(newValue: Float) {
        _uiState.update { it.copy(red = newValue.toInt()) }
    }
    
    fun onGreenChanged(newValue: Float) {
        // TODO: реализовать
    }
    
    fun onBlueChanged(newValue: Float) {
        // TODO: реализовать
    }
    
    fun generateRandomColor() {
        _uiState.update {
            ColorUiState(
                red = (0..255).random(),
                green = (0..255).random(),
                blue = (0..255).random()
            )
        }
    }
}
```

**Реализовать:**
1. Создать 3 Slider (0-255 диапазон)
2. Text с uiState.hexCode
3. Box с фоном uiState.color
4. Кнопка для randomColor()
5. Показать реактивное обновление при движении слайдеров

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

package com.example.depositapp.navigation

// Объект с маршрутами навигации
// sealed class — закрытая иерархия, нельзя добавить новый маршрут снаружи
// object — синглтон, не нужно создавать экземпляр
sealed class Screen(val route: String) {
    object Main    : Screen("main")       // Главный экран
    object Step1   : Screen("step1")      // Шаг 1: основные параметры
    object Step2   : Screen("step2")      // Шаг 2: доп. параметры
    object Result  : Screen("result")     // Экран результата
    object History : Screen("history")    // История расчётов
    object Detail  : Screen("detail/{id}") { // Детали одного расчёта
        // Функция для создания маршрута с конкретным id
        fun createRoute(id: Long) = "detail/$id"
    }
}

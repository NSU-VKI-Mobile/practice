package ci.nsu.moble.main


// Все маршруты в одном месте — легко менять и не ошибиться в строках
object Navigation {
    const val MAIN    = "main"
    const val STEP1   = "step1"
    const val STEP2   = "step2"
    const val RESULT  = "result"
    const val HISTORY = "history"
    const val DETAIL  = "detail/{id}"

    fun detail(id: Long) = "detail/$id"
}
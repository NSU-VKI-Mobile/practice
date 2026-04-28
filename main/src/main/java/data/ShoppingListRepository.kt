package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.database.ShoppingListDao
import ci.nsu.mobile.main.models.ShoppingList
import kotlinx.coroutines.flow.Flow

class ShoppingListRepository(private val dao: ShoppingListDao) {
    fun getAllLists(): Flow<List<ShoppingList>> = dao.getAllLists()

    suspend fun getListForDate(date: String): List<String> {
        return dao.getListByDate(date)?.items ?: emptyList()
    }

    suspend fun saveList(date: String, items: List<String>) {
        dao.insertList(ShoppingList(date, items))
    }

    suspend fun deleteList(date: String) {
        dao.deleteList(date)
    }
}
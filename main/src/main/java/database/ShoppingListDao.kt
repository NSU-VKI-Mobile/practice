package ci.nsu.mobile.main.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ci.nsu.mobile.main.models.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists")
    fun getAllLists(): Flow<List<ShoppingList>> //Flow - обновлять каждый рах

    @Query("SELECT * FROM shopping_lists WHERE date = :date")
    suspend fun getListByDate(date: String): ShoppingList? //получение бдд на дату

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(shoppingList: ShoppingList) //сохранение бд

    @Query("DELETE FROM shopping_lists WHERE date = :date")
    suspend fun deleteList(date: String)
}
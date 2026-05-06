package ci.nsu.mobile.main.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Query("SELECT * FROM cats ORDER BY dateAdded DESC")
    fun getAllCatsFlow(): Flow<List<CatEntity>>

    @Insert
    suspend fun insertCat(cat: CatEntity): Long

    @Delete
    suspend fun deleteCat(cat: CatEntity)

    @Query("DELETE FROM cats WHERE id IN (:ids)")
    suspend fun deleteCatsByIds(ids: List<Long>)
}
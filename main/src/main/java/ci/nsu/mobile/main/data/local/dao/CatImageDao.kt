package ci.nsu.mobile.main.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.data.local.entity.CatImage
import kotlinx.coroutines.flow.Flow

@Dao
interface CatImageDao {
    @Query("SELECT * FROM cat_images ORDER BY date DESC")
    fun getAllImages(): Flow<List<CatImage>>

    @Insert
    suspend fun insertImage(image: CatImage)

    @Delete
    suspend fun deleteImage(image: CatImage)

    @Query("DELETE FROM cat_images WHERE id IN (:ids)")
    suspend fun deleteImagesByIds(ids: List<Long>)
}
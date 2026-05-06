package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.db.CatDao
import ci.nsu.mobile.main.data.db.CatEntity
import kotlinx.coroutines.flow.Flow

class CatRepository(private val catDao: CatDao) {
    fun getAllCatsFlow(): Flow<List<CatEntity>> = catDao.getAllCatsFlow()

    suspend fun insertCat(cat: CatEntity): Long = catDao.insertCat(cat)

    suspend fun deleteCat(cat: CatEntity) = catDao.deleteCat(cat)

    suspend fun deleteCatsByIds(ids: List<Long>) = catDao.deleteCatsByIds(ids)
}
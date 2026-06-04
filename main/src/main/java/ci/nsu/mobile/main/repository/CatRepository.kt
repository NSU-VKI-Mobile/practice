package ci.nsu.mobile.main.repository

import android.content.Context
import ci.nsu.mobile.main.data.local.CatDatabase
import ci.nsu.mobile.main.data.local.entity.CatImage
import kotlinx.coroutines.flow.Flow
import java.util.Date

class CatRepository(context: Context) {
    private val catImageDao = CatDatabase.getDatabase(context).catImageDao()

    fun getAllImages(): Flow<List<CatImage>> = catImageDao.getAllImages()

    suspend fun saveImage(url: String) {
        val image = CatImage(
            url = url,
            date = Date()
        )
        catImageDao.insertImage(image)
    }

    suspend fun deleteImage(image: CatImage) {
        catImageDao.deleteImage(image)
    }

    suspend fun deleteImages(images: List<CatImage>) {
        val ids = images.map { it.id }
        catImageDao.deleteImagesByIds(ids)
    }
}
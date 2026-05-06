package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.db.CatDatabase
import ci.nsu.mobile.main.data.repository.CatRepository

class CatApplication : Application() {
    val database by lazy { CatDatabase.getDatabase(this) }
    val repository by lazy { CatRepository(database.catDao()) }
}
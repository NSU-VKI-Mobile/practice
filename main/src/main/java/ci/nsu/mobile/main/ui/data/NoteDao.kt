package ci.nsu.mobile.main.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE date = :date ORDER BY id DESC")
    fun getNotesByDate(date: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE date = :date")
    suspend fun getNotesByDateSync(date: String): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("SELECT COUNT(*) FROM notes WHERE date = :date")
    suspend fun getNoteCountByDate(date: String): Int

    @Query("SELECT COUNT(*) FROM notes WHERE date = :date AND isCompleted = 1")
    suspend fun getCompletedCountByDate(date: String): Int
}
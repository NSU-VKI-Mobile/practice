package ci.nsu.mobile.main.ui.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class NoteRepository(private val noteDao: NoteDao) {

    fun getNotesByDate(date: LocalDate): Flow<List<NoteEntity>> {
        return noteDao.getNotesByDate(date.toString())
    }

    suspend fun getNotesByDateSync(date: LocalDate): List<NoteEntity> {
        return noteDao.getNotesByDateSync(date.toString())
    }

    suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteNoteById(noteId: Int) {
        noteDao.deleteNoteById(noteId)
    }

    suspend fun getNoteCountByDate(date: LocalDate): Int {
        return noteDao.getNoteCountByDate(date.toString())
    }

    suspend fun getCompletedCountByDate(date: LocalDate): Int {
        return noteDao.getCompletedCountByDate(date.toString())
    }
}
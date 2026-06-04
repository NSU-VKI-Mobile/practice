package ci.nsu.mobile.main.ui.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class NoteRepository(private val noteDao: NoteDao) {

    fun getNotesByDate(date: LocalDate): Flow<List<NoteEntity>> {
        return noteDao.getNotesByDate(date.toString()).map { notes ->
            // Фильтруем на уровне потока для безопасности
            notes.filter { it.date == date.toString() }
        }
    }

    suspend fun getNotesByDateSync(date: LocalDate): List<NoteEntity> {
        val notes = noteDao.getNotesByDateSync(date.toString())
        // Фильтруем синхронные запросы
        return notes.filter { it.date == date.toString() }
    }

    suspend fun insertNote(note: NoteEntity) {
        // Валидация перед вставкой
        require(note.date.isNotBlank()) { "Date cannot be blank" }
        require(note.text.isNotBlank()) { "Note text cannot be blank" }
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        require(note.date.isNotBlank()) { "Date cannot be blank" }
        require(note.text.isNotBlank()) { "Note text cannot be blank" }
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
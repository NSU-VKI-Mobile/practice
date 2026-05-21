package ci.nsu.mobile.main.ui.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.ui.data.NoteEntity
import ci.nsu.mobile.main.ui.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class NoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    fun loadNotes(date: LocalDate) {
        viewModelScope.launch {
            repository.getNotesByDate(date).collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    suspend fun getNotesSync(date: LocalDate): List<NoteEntity> {
        return repository.getNotesByDateSync(date)
    }

    fun addNote(date: LocalDate, text: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val note = NoteEntity(
                date = date.toString(),
                text = text,
                isCompleted = false
            )
            repository.insertNote(note)
            onComplete()
        }
    }

    fun updateNote(note: NoteEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.updateNote(note)
            onComplete()
        }
    }

    fun deleteNote(note: NoteEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteNote(note)
            onComplete()
        }
    }

    fun deleteNoteById(noteId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteNoteById(noteId)
            onComplete()
        }
    }

    fun toggleNoteCompletion(note: NoteEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            val updatedNote = note.copy(isCompleted = !note.isCompleted)
            repository.updateNote(updatedNote)
            onComplete()
        }
    }

    suspend fun getNoteCount(date: LocalDate): Int {
        return repository.getNoteCountByDate(date)
    }

    suspend fun getCompletedCount(date: LocalDate): Int {
        return repository.getCompletedCountByDate(date)
    }
}
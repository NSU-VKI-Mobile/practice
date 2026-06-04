package ci.nsu.mobile.main.ui.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // Добавляем текущую дату для отслеживания
    private var currentDate: LocalDate? = null

    fun loadNotes(date: LocalDate) {
        // Проверяем, не загружаем ли мы ту же дату
        if (currentDate == date && _notes.value.isNotEmpty()) {
            return
        }
        currentDate = date

        viewModelScope.launch {
            repository.getNotesByDate(date).collect { notesList ->
                // Фильтруем заметки по дате на всякий случай
                val filteredNotes = notesList.filter { it.date == date.toString() }
                _notes.value = filteredNotes
            }
        }
    }

    // Метод для принудительной перезагрузки
    fun refreshNotes(date: LocalDate) {
        currentDate = null
        loadNotes(date)
    }

    suspend fun getNotesSync(date: LocalDate): List<NoteEntity> {
        val notes = repository.getNotesByDateSync(date)
        // Фильтруем на уровне репозитория
        return notes.filter { it.date == date.toString() }
    }

    fun addNote(date: LocalDate, text: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val note = NoteEntity(
                date = date.toString(),
                text = text,
                isCompleted = false
            )
            repository.insertNote(note)
            // Обновляем список после добавления
            loadNotes(date)
            onComplete()
        }
    }

    fun updateNote(note: NoteEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.updateNote(note)
            // Обновляем текущую дату если есть
            currentDate?.let { loadNotes(it) }
            onComplete()
        }
    }

    fun deleteNote(note: NoteEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteNote(note)
            currentDate?.let { loadNotes(it) }
            onComplete()
        }
    }

    fun deleteNoteById(noteId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteNoteById(noteId)
            currentDate?.let { loadNotes(it) }
            onComplete()
        }
    }

    fun toggleNoteCompletion(note: NoteEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            val updatedNote = note.copy(isCompleted = !note.isCompleted)
            repository.updateNote(updatedNote)
            currentDate?.let { loadNotes(it) }
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
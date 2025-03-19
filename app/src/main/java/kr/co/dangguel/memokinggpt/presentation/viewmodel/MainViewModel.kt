package kr.co.dangguel.memokinggpt.presentation.viewmodel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.usecase.FolderUseCase
import kr.co.dangguel.memokinggpt.domain.usecase.NoteUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val folderUseCase: FolderUseCase,
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _folders = MutableStateFlow<List<FolderEntity>>(emptyList())
    val folders: StateFlow<List<FolderEntity>> = _folders

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes

    // ✅ 폴더 + 노트 리스트 Flow
    val items: StateFlow<List<Item>> = combine(_folders, _notes) { folders, notes ->
        val folderItems = folders.map { Item.FolderItem(it) }
        val noteItems = notes.map { Item.NoteItem(it) }
        folderItems + noteItems
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            insertDummyData() // ✅ 데이터가 없는 경우에만 더미 데이터 삽입
            fetchData() // ✅ 데이터 불러오기
        }
    }

    /**
     * ✅ 더미 데이터 삽입 (데이터가 없는 경우에만 삽입)
     */
    private fun insertDummyData() {
        viewModelScope.launch(Dispatchers.IO) {
            val existingFolders = folderUseCase.getRootFolders().firstOrNull() ?: emptyList()
            val existingNotes = noteUseCase.getAllNotes().firstOrNull() ?: emptyList()

            // ✅ 폴더 & 노트가 없는 경우에만 기본 데이터 삽입
            if (existingFolders.isEmpty() && existingNotes.isEmpty()) {
                val folderIds = mutableListOf<Long>()

                // ✅ 폴더 삽입 후 정확한 ID 가져오기
                val defaultFolders = listOf(
                    FolderEntity(name = "Work"),
                    FolderEntity(name = "Personal"),
                    FolderEntity(name = "Ideas")
                )
                for (folder in defaultFolders) {
                    val folderId = folderUseCase.insertFolder(folder)
                    folderIds.add(folderId)
                }

                // ✅ 노트 삽입 (폴더가 없는 상태에서 삽입하지 않도록 체크)
                if (folderIds.isNotEmpty()) {
                    val defaultNotes = listOf(
                        NoteEntity(folderId = folderIds[0], title = "Meeting Notes", content = "Discuss project timeline."),
                        NoteEntity(folderId = folderIds[1], title = "Grocery List", content = "Buy milk, eggs, and bread."),
                        NoteEntity(folderId = folderIds[2], title = "App Ideas", content = "Create an AI-powered note-taking app."),
                        NoteEntity(folderId = null, title = "How to Use", content = "Tap the camera icon at the top to select an image and extract text easily.\n" +
                                "Tap the star icon at the top to experience the AI-powered summarization feature.") // ✅ 폴더 없는 노트 추가
                    )
                    defaultNotes.forEach { noteUseCase.insertNote(it) }
                }
            }
        }
    }

    /**
     * ✅ 폴더 및 노트 데이터 불러오기
     */
    private fun fetchData() {
        viewModelScope.launch {
            folderUseCase.getRootFolders().collect { _folders.value = it }
        }
        viewModelScope.launch {
            noteUseCase.getAllNotes().collect { _notes.value = it }
        }
    }

    fun onDeleteFolder(folderId: Long) {
        viewModelScope.launch {
            folderUseCase.deleteFolderWithNotes(folderId)
        }
    }

    fun onDeleteNote(noteId: Long) {
        viewModelScope.launch {
            noteUseCase.deleteNoteById(noteId) // ✅ 노트 삭제를 UseCase에서 처리
        }
    }
}

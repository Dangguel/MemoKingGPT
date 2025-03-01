package kr.co.dangguel.memokinggpt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
            resetDatabaseAndInsertDummyData()
            fetchData()
        }
    }

    // ✅ 트랜잭션을 활용한 데이터 리셋 및 더미 데이터 삽입
    private suspend fun resetDatabaseAndInsertDummyData() {
        viewModelScope.launch {
            // ✅ 트랜잭션 시작
            try {
                // ✅ 기존 데이터 삭제 (노트 → 폴더 순서)
                noteUseCase.deleteAllNotes()
                folderUseCase.deleteAllFolders()

                // ✅ 폴더 삽입 후 ID를 정확하게 받아오기
                val folderIds = mutableListOf<Long>()
                val defaultFolders = listOf(
                    FolderEntity(name = "Work"),
                    FolderEntity(name = "Personal"),
                    FolderEntity(name = "Ideas")
                )

                for (folder in defaultFolders) {
                    val folderId = folderUseCase.insertFolder(folder)
                    folderIds.add(folderId)
                }

                // ✅ 폴더 ID가 존재하는지 확인 후 노트 삽입
                if (folderIds.isNotEmpty()) {
                    val defaultNotes = listOf(
                        // 🔹 폴더 안에 있는 노트 (존재하는 폴더 ID만 사용)
                        NoteEntity(folderId = folderIds[0], title = "Meeting Notes", content = "Discuss project timeline."),
                        NoteEntity(folderId = folderIds[1], title = "Grocery List", content = "Buy milk, eggs, and bread."),
                        NoteEntity(folderId = folderIds[2], title = "App Ideas", content = "Create an AI-powered note-taking app."),

                        // 🔹 폴더에 속하지 않는 노트 (folderId = null)
                        NoteEntity(folderId = null, title = "General Note", content = "This note is not inside any folder.")
                    )
                    defaultNotes.forEach { noteUseCase.insertNote(it) }
                }
                // ✅ 트랜잭션 성공
            } catch (e: Exception) {
                // ✅ 트랜잭션 실패 시 롤백
                e.printStackTrace()
            }
        }
    }

    // ✅ 폴더 및 노트 데이터 불러오기
    private fun fetchData() {
        viewModelScope.launch {
            folderUseCase.getRootFolders().collect { _folders.value = it }
        }
        viewModelScope.launch {
            noteUseCase.getAllNotes().collect { _notes.value = it }
        }
    }
}
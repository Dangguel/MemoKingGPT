package kr.co.dangguel.memokinggpt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.model.SortOrder
import kr.co.dangguel.memokinggpt.domain.usecase.FolderUseCase
import kr.co.dangguel.memokinggpt.domain.usecase.NoteUseCase
import javax.inject.Inject

sealed class Item {
    data class FolderItem(val folder: FolderEntity) : Item()
    data class NoteItem(val note: NoteEntity) : Item()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val folderUseCase: FolderUseCase,
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _folders = MutableStateFlow<List<FolderEntity>>(emptyList())
    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())

    private val _sortOrder = MutableStateFlow(SortOrder.TITLE)
    private val _isAscending = MutableStateFlow(true)

    val sortOrder: StateFlow<SortOrder> = _sortOrder
    val isAscending: StateFlow<Boolean> = _isAscending

    fun updateSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun toggleSortDirection() {
        _isAscending.value = !_isAscending.value
    }

    // ✅ 폴더와 노트를 정렬하여 Flow로 제공 (폴더는 항상 위, 폴더끼리만 정렬 방향 변경)
    val items: StateFlow<List<Item>> = combine(_folders, _notes, _sortOrder, _isAscending) { folders, notes, sortOrder, isAscending ->

        // 1️⃣ 폴더 정렬 (폴더끼리만 정렬 방향 변경, 항상 위)
        val sortedFolders = when (sortOrder) {
            SortOrder.TITLE -> if (isAscending) folders.sortedBy { it.name } else folders.sortedByDescending { it.name }
            SortOrder.CREATED_DATE -> if (isAscending) folders.sortedBy { it.createdAt } else folders.sortedByDescending { it.createdAt }
            SortOrder.UPDATED_DATE -> if (isAscending) folders.sortedBy { it.updatedAt } else folders.sortedByDescending { it.updatedAt }
        }

        // 2️⃣ 노트 정렬 (노트끼리만 정렬 방향 변경, 항상 폴더 아래)
        val sortedNotes = when (sortOrder) {
            SortOrder.TITLE -> if (isAscending) notes.sortedBy { it.title } else notes.sortedByDescending { it.title }
            SortOrder.CREATED_DATE -> if (isAscending) notes.sortedBy { it.createdAt } else notes.sortedByDescending { it.createdAt }
            SortOrder.UPDATED_DATE -> if (isAscending) notes.sortedBy { it.updatedAt } else notes.sortedByDescending { it.updatedAt }
        }

        val folderItems = sortedFolders.map { Item.FolderItem(it) }
        val noteItems = sortedNotes.map { Item.NoteItem(it) }

        folderItems + noteItems // ✅ 폴더는 항상 위에 유지됨, 노트는 그 아래에서만 정렬됨
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            folderUseCase.getRootFolders().collect { _folders.value = it }
        }
        viewModelScope.launch {
            noteUseCase.getAllNotes().collect { _notes.value = it }
        }
    }

    suspend fun insertDummyData() {
        viewModelScope.launch {
            if (_folders.value.isEmpty()) {
                val folder1 = FolderEntity(name = "일반", parentFolderId = null)
                val folder2 = FolderEntity(name = "작업", parentFolderId = null)
                val folder3 = FolderEntity(name = "프로젝트", parentFolderId = null)

                folderUseCase.insertFolder(folder1)
                folderUseCase.insertFolder(folder2)
                folderUseCase.insertFolder(folder3)
            }

            if (_notes.value.isEmpty()) {
                noteUseCase.insertNote(
                    NoteEntity(folderId = 1, title = "노트 1", content = "테스트 내용 1")
                )
                noteUseCase.insertNote(
                    NoteEntity(folderId = 2, title = "작업 관련 노트", content = "작업 노트 내용")
                )
                noteUseCase.insertNote(
                    NoteEntity(folderId = 3, title = "프로젝트 메모", content = "프로젝트 아이디어")
                )
            }
        }
    }
}

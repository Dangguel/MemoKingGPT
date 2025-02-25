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

@HiltViewModel
class MainViewModel @Inject constructor(
    private val folderUseCase: FolderUseCase,
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _folders = MutableStateFlow<List<FolderEntity>>(emptyList())
    val folders: StateFlow<List<FolderEntity>> = _folders

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes

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

    val items: StateFlow<List<Item>> = combine(_folders, _notes, _sortOrder, _isAscending) { folders, notes, sortOrder, isAscending ->
        val sortedFolders = when (sortOrder) {
            SortOrder.TITLE -> if (isAscending) folders.sortedBy { it.name } else folders.sortedByDescending { it.name }
            SortOrder.CREATED_DATE -> if (isAscending) folders.sortedBy { it.createdAt } else folders.sortedByDescending { it.createdAt }
            SortOrder.UPDATED_DATE -> if (isAscending) folders.sortedBy { it.updatedAt } else folders.sortedByDescending { it.updatedAt }
        }

        val sortedNotes = when (sortOrder) {
            SortOrder.TITLE -> if (isAscending) notes.sortedBy { it.title } else notes.sortedByDescending { it.title }
            SortOrder.CREATED_DATE -> if (isAscending) notes.sortedBy { it.createdAt } else notes.sortedByDescending { it.createdAt }
            SortOrder.UPDATED_DATE -> if (isAscending) notes.sortedBy { it.updatedAt } else notes.sortedByDescending { it.updatedAt }
        }

        val folderItems = sortedFolders.map { Item.FolderItem(it) }
        val noteItems = sortedNotes.map { Item.NoteItem(it) }

        folderItems + noteItems
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
}

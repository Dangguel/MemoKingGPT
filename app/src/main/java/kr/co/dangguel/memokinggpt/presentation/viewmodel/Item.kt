package kr.co.dangguel.memokinggpt.presentation.viewmodel

import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

sealed class Item {
    data class FolderItem(val folder: FolderEntity) : Item()
    data class NoteItem(val note: NoteEntity) : Item()
}
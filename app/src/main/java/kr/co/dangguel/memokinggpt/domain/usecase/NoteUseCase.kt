package kr.co.dangguel.memokinggpt.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.repository.NoteRepository
import javax.inject.Inject

class NoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend fun getNoteById(noteId: Long): NoteEntity? = repository.getNoteById(noteId)
    fun getAllNotes(): Flow<List<NoteEntity>> = repository.getAllNotes() // ✅ 노트 전체 조회 추가
    suspend fun insertNote(note: NoteEntity): Long = repository.insertNote(note)
    suspend fun updateNote(note: NoteEntity) = repository.updateNote(note)
    suspend fun deleteNote(note: NoteEntity) = repository.deleteNote(note)
    suspend fun deleteAllNotes() = repository.deleteAllNotes()
    suspend fun deleteNoteById(noteId: Long) = repository.deleteNoteById(noteId)
    suspend fun getNotesByFolderId(folderId: Long) = repository.getNotesByFolderId(folderId)
}

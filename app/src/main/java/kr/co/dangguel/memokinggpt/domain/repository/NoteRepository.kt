package kr.co.dangguel.memokinggpt.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

interface NoteRepository {
    suspend fun getNoteById(noteId: Long): NoteEntity? // ✅ 노트 ID로 가져오기
    fun getAllNotes(): Flow<List<NoteEntity>>
    suspend fun insertNote(note: NoteEntity): Long
    suspend fun updateNote(note: NoteEntity) // ✅ 업데이트 기능 추가
    suspend fun deleteNote(note: NoteEntity)
    suspend fun deleteAllNotes()
    suspend fun deleteNoteById(noteId: Long) // ✅ 추가
    suspend fun getNotesByFolderId(folderId: Long): Flow<List<NoteEntity>> // ✅ 추가
}

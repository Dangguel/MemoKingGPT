package kr.co.dangguel.memokinggpt.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    suspend fun insertNote(note: NoteEntity): Long
    suspend fun deleteNote(note: NoteEntity)
    suspend fun deleteAllNotes()
}

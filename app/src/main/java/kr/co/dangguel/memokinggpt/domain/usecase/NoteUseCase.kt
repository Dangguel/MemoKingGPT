package kr.co.dangguel.memokinggpt.domain.usecase

import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.repository.NoteRepository
import javax.inject.Inject

class NoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend fun getAllNotes() = noteRepository.getAllNotes()

    suspend fun insertNote(note: NoteEntity): Long {
        return noteRepository.insertNote(note)
    }

    suspend fun deleteAllNotes() {
        noteRepository.deleteAllNotes()
    }
}

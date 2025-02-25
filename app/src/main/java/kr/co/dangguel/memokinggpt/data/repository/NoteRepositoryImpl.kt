package kr.co.dangguel.memokinggpt.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.dao.NoteDao
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    override suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    override suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }
}

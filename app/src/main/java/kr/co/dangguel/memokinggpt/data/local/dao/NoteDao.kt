package kr.co.dangguel.memokinggpt.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE id = :noteId LIMIT 1")
    suspend fun getNoteById(noteId: Long): NoteEntity? // ✅ 노트 ID로 단일 노트 가져오기

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity) // ✅ 업데이트 기능 추가

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("DELETE FROM notes WHERE id = :noteId") // ✅ 추가
    suspend fun deleteNoteById(noteId: Long)

    @Query("SELECT * FROM notes WHERE folderId = :folderId")
    fun getNotesByFolderId(folderId: Long): Flow<List<NoteEntity>>
}


package kr.co.dangguel.memokinggpt.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}

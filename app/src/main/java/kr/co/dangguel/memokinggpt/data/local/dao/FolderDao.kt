package kr.co.dangguel.memokinggpt.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders")
    fun getAllFolders(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folders WHERE parentFolderId IS NULL")
    fun getRootFolders(): Flow<List<FolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity): Long

    @Query("DELETE FROM folders WHERE id = :folderId") // ✅ 기존 FolderEntity 삭제 -> Long ID 기반 삭제
    suspend fun deleteFolderById(folderId: Long)

    @Query("DELETE FROM folders")
    suspend fun deleteAllFolders()
}

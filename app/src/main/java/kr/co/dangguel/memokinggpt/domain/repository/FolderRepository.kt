package kr.co.dangguel.memokinggpt.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity

interface FolderRepository {
    fun getAllFolders(): Flow<List<FolderEntity>>
    fun getRootFolders(): Flow<List<FolderEntity>>
    suspend fun insertFolder(folder: FolderEntity): Long
    suspend fun deleteFolder(folderId: Long)
    suspend fun deleteAllFolders()
}

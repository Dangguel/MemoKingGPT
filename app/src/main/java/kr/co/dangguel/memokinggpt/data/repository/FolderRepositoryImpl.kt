package kr.co.dangguel.memokinggpt.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.dangguel.memokinggpt.data.local.dao.FolderDao
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.domain.repository.FolderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao
) : FolderRepository {

    override fun getAllFolders(): Flow<List<FolderEntity>> = folderDao.getAllFolders()

    override fun getRootFolders(): Flow<List<FolderEntity>> = folderDao.getRootFolders()

    override suspend fun insertFolder(folder: FolderEntity): Long {
        return folderDao.insertFolder(folder)
    }

    override suspend fun deleteFolder(folder: FolderEntity) {
        folderDao.deleteFolder(folder)
    }

    override suspend fun deleteAllFolders() {
        folderDao.deleteAllFolders()
    }
}

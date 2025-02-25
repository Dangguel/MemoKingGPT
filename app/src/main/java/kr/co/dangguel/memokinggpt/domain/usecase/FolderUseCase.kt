package kr.co.dangguel.memokinggpt.domain.usecase

import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.domain.repository.FolderRepository
import javax.inject.Inject

class FolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    suspend fun getRootFolders() = folderRepository.getRootFolders()

    suspend fun insertFolder(folder: FolderEntity): Long {
        return folderRepository.insertFolder(folder)
    }

    suspend fun deleteAllFolders() {
        folderRepository.deleteAllFolders()
    }
}

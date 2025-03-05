package kr.co.dangguel.memokinggpt.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.domain.repository.FolderRepository
import kr.co.dangguel.memokinggpt.domain.repository.NoteRepository
import javax.inject.Inject

class FolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository
) {
    suspend fun getRootFolders() = folderRepository.getRootFolders()

    suspend fun insertFolder(folder: FolderEntity): Long {
        return folderRepository.insertFolder(folder)
    }

    suspend fun deleteAllFolders() {
        folderRepository.deleteAllFolders()
    }

    suspend fun deleteFolderWithNotes(folderId: Long) {
        // ✅ 폴더 안에 있는 모든 노트를 가져옴
        val notes = noteRepository.getNotesByFolderId(folderId).first() // Flow를 리스트로 변환

        // ✅ 해당 폴더의 노트들을 먼저 삭제
        notes.forEach { note ->
            noteRepository.deleteNote(note) // ✅ 개별 노트 삭제
        }

        // ✅ 폴더 삭제
        folderRepository.deleteFolder(folderId)
    }
}

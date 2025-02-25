package kr.co.dangguel.memokinggpt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val parentFolderId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(), // ✅ 추가
    val updatedAt: Long = System.currentTimeMillis()  // ✅ 추가
)

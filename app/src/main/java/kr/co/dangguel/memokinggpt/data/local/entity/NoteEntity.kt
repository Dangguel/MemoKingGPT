package kr.co.dangguel.memokinggpt.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE  // ✅ 폴더 삭제 시 해당 폴더의 노트도 자동 삭제
        )
    ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val folderId: Long? = null,  // ✅ 삭제된 폴더의 id가 참조되면 오류 발생 -> CASCADE 처리 필요, null 허용
    val title: String,
    val content: String,
    val thumbnailPath: String = "",
    val createdAt: Long = System.currentTimeMillis(), // ✅ 추가
    val updatedAt: Long = System.currentTimeMillis()  // ✅ 추가
)
package kr.co.dangguel.memokinggpt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.co.dangguel.memokinggpt.data.local.dao.FolderDao
import kr.co.dangguel.memokinggpt.data.local.dao.NoteDao
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

@Database(entities = [FolderEntity::class, NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun noteDao(): NoteDao
}

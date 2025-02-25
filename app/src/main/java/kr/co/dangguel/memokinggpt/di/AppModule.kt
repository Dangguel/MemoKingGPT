package kr.co.dangguel.memokinggpt.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.dangguel.memokinggpt.data.local.AppDatabase
import kr.co.dangguel.memokinggpt.data.local.dao.FolderDao
import kr.co.dangguel.memokinggpt.data.local.dao.NoteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "memo_king_gpt.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFolderDao(database: AppDatabase): FolderDao = database.folderDao()

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()
}

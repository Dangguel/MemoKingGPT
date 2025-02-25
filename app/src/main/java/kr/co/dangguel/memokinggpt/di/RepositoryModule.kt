package kr.co.dangguel.memokinggpt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.dangguel.memokinggpt.data.repository.FolderRepositoryImpl
import kr.co.dangguel.memokinggpt.data.repository.NoteRepositoryImpl
import kr.co.dangguel.memokinggpt.domain.repository.FolderRepository
import kr.co.dangguel.memokinggpt.domain.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFolderRepository(
        impl: FolderRepositoryImpl
    ): FolderRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository
}

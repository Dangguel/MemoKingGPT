package kr.co.dangguel.memokinggpt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.dangguel.memokinggpt.domain.usecase.OcrUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideOcrUseCase(): OcrUseCase {
        return OcrUseCase()
    }
}

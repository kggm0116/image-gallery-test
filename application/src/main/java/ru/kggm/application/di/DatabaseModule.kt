package ru.kggm.application.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kggm.data.daos.FileDao
import ru.kggm.data.daos.FolderDao
import ru.kggm.data.database.ImageGalleryDatabase
import ru.kggm.data.repositories.FileRepositoryImpl
import ru.kggm.data.repositories.FolderRepositoryImpl
import ru.kggm.domain.repositories.FileRepository
import ru.kggm.domain.repositories.FolderRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ImageGalleryDatabase
            = ImageGalleryDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideFileDao(database: ImageGalleryDatabase): FileDao = database.fileDao()

    @Provides
    @Singleton
    fun provideFolderDao(database: ImageGalleryDatabase): FolderDao = database.folderDao()

    @Provides
    @Singleton
    fun provideFolderRepository(folderDao: FolderDao): FolderRepository = FolderRepositoryImpl(folderDao)

    @Provides
    @Singleton
    fun provideFileRepository(fileDao: FileDao): FileRepository = FileRepositoryImpl(fileDao)
}
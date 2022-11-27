package com.example.imagegallerytest.data.repositories

import androidx.annotation.WorkerThread
import com.example.imagegallerytest.data.database.daos.FolderDao
import com.example.imagegallerytest.data.entities.Folder
import com.example.imagegallerytest.data.entities.FolderWithFiles
import kotlinx.coroutines.flow.Flow

class FolderRepository(private val folderDao: FolderDao) {
    val allFolders: Flow<List<Folder>> = folderDao.getAll()
    val allFoldersWithFiles: Flow<List<FolderWithFiles>> = folderDao.getFoldersWithFiles()
    @WorkerThread
    suspend fun insert(folder: Folder): Long {
        return folderDao.insert(folder)
    }
    @WorkerThread
    suspend fun insert(folders: List<Folder>) {
        folderDao.insert(folders)
    }
    @WorkerThread
    suspend fun update(folder: Folder) {
        folderDao.update(folder)
    }
    @WorkerThread
    suspend fun update(folders: List<Folder>) {
        folderDao.update(folders)
    }
    @WorkerThread
    suspend fun delete(folder: Folder) {
        folderDao.delete(folder)
    }
    @WorkerThread
    suspend fun delete(folders: List<Folder>) {
        folderDao.delete(folders)
    }
    @WorkerThread
    suspend fun countFiles(folder: Folder): Int {
        return folderDao.countFiles(folder.uri)
    }
}
package ru.kggm.imagegallerytest.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import ru.kggm.imagegallerytest.data.database.daos.FileDao
import ru.kggm.imagegallerytest.data.entities.File
import kotlin.system.measureTimeMillis

class FileRepository(private val fileDao: FileDao) {
    val allFiles: Flow<List<File>> = fileDao.getAll()
    @WorkerThread
    suspend fun insert(file: File) : Long {
        return fileDao.insert(file)
    }
    @WorkerThread
    suspend fun insert(files: List<File>) {
        fileDao.insert(files)
    }
    @WorkerThread
    suspend fun update(file: File) {
        fileDao.update(file)
    }
    @WorkerThread
    suspend fun update(files: List<File>) {
        fileDao.update(files)
    }
    @WorkerThread
    suspend fun delete(file: File) {
        fileDao.delete(file)
    }
    @WorkerThread
    suspend fun delete(files: List<File>) {
        fileDao.delete(files)
    }
    @WorkerThread
    suspend fun deleteExcept(files: List<File>) {
        fileDao.deleteExcept(files.map { it.uri })
    }
}
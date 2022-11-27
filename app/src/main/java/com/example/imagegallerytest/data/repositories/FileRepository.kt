package com.example.imagegallerytest.data.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.imagegallerytest.data.database.daos.FileDao
import com.example.imagegallerytest.data.entities.File
import kotlinx.coroutines.flow.Flow
import kotlin.system.measureTimeMillis

class FileRepository(private val fileDao: FileDao) {
    val allFiles: Flow<List<File>> = fileDao.getAll()
    @WorkerThread
    suspend fun insert(file: File) : Long {
        return fileDao.insert(file)
    }
    @WorkerThread
    suspend fun insert(files: List<File>) {
        val time = measureTimeMillis { fileDao.insert(files) }
        Log.println(Log.INFO, "", "Inserted ${files.size} in ${time}ms")
    }
    @WorkerThread
    suspend fun update(file: File) {
        fileDao.update(file)
    }
    @WorkerThread
    suspend fun update(files: List<File>) {
        val time = measureTimeMillis { fileDao.update(files) }
        Log.println(Log.INFO, "", "Updated ${files.size} in ${time}ms")
    }
    @WorkerThread
    suspend fun delete(file: File) {
        fileDao.delete(file)
    }
    @WorkerThread
    suspend fun delete(files: List<File>) {
        val time = measureTimeMillis { fileDao.delete(files) }
        Log.println(Log.INFO, "", "Deleted ${files.size} in ${time}ms")
    }
    @WorkerThread
    suspend fun deleteExcept(files: List<File>) {
        fileDao.deleteExcept(files.map { it.uri })
    }
}
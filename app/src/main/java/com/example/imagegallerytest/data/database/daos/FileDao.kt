package com.example.imagegallerytest.data.database.daos

import android.net.Uri
import androidx.room.*
import com.example.imagegallerytest.data.entities.File
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("SELECT * FROM ${File.TABLE} ORDER BY ${File.NAME} ASC")
    fun getAll(): Flow<List<File>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(file: File): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(files: List<File>)

    @Update
    suspend fun update(file: File)
    @Update
    suspend fun update(files: List<File>)

    @Delete
    suspend fun delete(file: File)
    @Delete
    suspend fun delete(files: List<File>)

    @Query("DELETE FROM ${File.TABLE} " +
            "WHERE ${File.URI} NOT IN (:fileUris)")
    suspend fun deleteExcept(fileUris: List<Uri>)

    @Transaction
    @Query("DELETE FROM ${File.TABLE}")
    suspend fun deleteAll()
}
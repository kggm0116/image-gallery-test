package ru.kggm.imagegallerytest.data.database.daos

import android.net.Uri
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kggm.imagegallerytest.data.entities.File
import ru.kggm.imagegallerytest.data.entities.Folder
import ru.kggm.imagegallerytest.data.entities.FolderWithFiles

@Dao
interface FolderDao {
    @Query("SELECT * FROM ${Folder.TABLE} ORDER BY ${Folder.NAME} ASC")
    fun getAll(): Flow<List<Folder>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: Folder): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folders: List<Folder>)

    @Update
    suspend fun update(folder: Folder)
    @Update
    suspend fun update(folders: List<Folder>)

    @Delete
    suspend fun delete(folder: Folder)
    @Delete
    suspend fun delete(folders: List<Folder>)

    @Query("DELETE FROM ${Folder.TABLE}")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM ${Folder.TABLE}")
    fun getFoldersWithFiles(): Flow<List<FolderWithFiles>>


    @Query("SELECT COUNT(*) FROM ${File.TABLE} " +
            "WHERE ${File.FOLDER_URI} = :folderUri")
    suspend fun countFiles(folderUri: Uri): Int
}
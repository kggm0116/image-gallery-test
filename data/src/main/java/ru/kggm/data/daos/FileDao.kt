package ru.kggm.data.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kggm.data.entities.FileEntity

@Dao
interface FileDao: BaseDao<FileEntity> {
    @Query("SELECT * FROM ${FileEntity.TABLE} ORDER BY ${FileEntity.URI} ASC")
    fun getAll(): Flow<List<FileEntity>>
}
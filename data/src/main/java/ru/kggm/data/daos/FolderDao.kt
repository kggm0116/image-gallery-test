package ru.kggm.data.daos

import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import ru.kggm.data.entities.FolderEntity

@Dao
interface FolderDao: BaseDao<FolderEntity> {
    @Query("SELECT * FROM ${FolderEntity.TABLE} ORDER BY ${FolderEntity.URI} ASC")
    fun getAll(): Flow<List<FolderEntity>>
}
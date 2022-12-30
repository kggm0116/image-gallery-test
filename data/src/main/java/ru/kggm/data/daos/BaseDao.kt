package ru.kggm.data.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(obj: T)
    @Insert
    suspend fun insert(objects: Collection<T>)
    @Update
    suspend fun update(obj: T)
    @Update
    suspend fun update(objects: Collection<T>)
    @Delete
    suspend fun delete(obj: T)
    @Delete
    suspend fun delete(objects: Collection<T>)
}
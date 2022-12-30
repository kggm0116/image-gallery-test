package ru.kggm.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kggm.domain.entities.*

interface FolderRepository {
    fun get(): Flow<List<Folder>>
    suspend fun add(folder: Folder)
    suspend fun add(folders: List<Folder>)
    suspend fun update(folder: Folder)
    suspend fun update(folders: List<Folder>)
    suspend fun remove(folder: Folder)
    suspend fun remove(folders: List<Folder>)
}
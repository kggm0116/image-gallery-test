package ru.kggm.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kggm.domain.entities.*

interface FileRepository {
    fun get(): Flow<Collection<File>>
    suspend fun add(file: File)
    suspend fun add(files: List<File>)
    suspend fun remove(file: File)
    suspend fun remove(files: List<File>)
}
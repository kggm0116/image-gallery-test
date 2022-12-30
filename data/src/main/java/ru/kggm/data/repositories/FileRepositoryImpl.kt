package ru.kggm.data.repositories

import kotlinx.coroutines.flow.map
import ru.kggm.data.daos.FileDao
import ru.kggm.data.entities.FileEntity.Companion.toDataEntity
import ru.kggm.data.entities.FileEntity.Companion.toDomainEntity
import ru.kggm.domain.entities.File
import ru.kggm.domain.repositories.FileRepository

class FileRepositoryImpl(
    private val fileDao: FileDao
) : FileRepository {
    override fun get() = fileDao.getAll()
        .map { files ->
            files.mapNotNull {
                try {
                    it.toDomainEntity()
                }
                catch (_: IllegalArgumentException) {
                    fileDao.delete(it)
                    null
                }
            }
        }
    override suspend fun add(file: File) {
        fileDao.insert(file.toDataEntity())
    }
    override suspend fun add(files: List<File>) {
        fileDao.insert(files.map { it.toDataEntity() })
    }
    override suspend fun remove(file: File) {
        fileDao.delete(file.toDataEntity())
    }
    override suspend fun remove(files: List<File>) {
        fileDao.delete(files.map { it.toDataEntity() })
    }
}
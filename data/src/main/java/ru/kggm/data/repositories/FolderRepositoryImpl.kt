package ru.kggm.data.repositories

import kotlinx.coroutines.flow.map
import ru.kggm.data.daos.FolderDao
import ru.kggm.data.entities.FolderEntity.Companion.toDataEntity
import ru.kggm.data.entities.FolderEntity.Companion.toDomainEntity
import ru.kggm.domain.entities.Folder
import ru.kggm.domain.repositories.FolderRepository

class FolderRepositoryImpl(
    private val folderDao: FolderDao
) : FolderRepository {
    override fun get() = folderDao.getAll()
        .map { folders ->
            folders.mapNotNull {
                try {
                    it.toDomainEntity()
                }
                catch (_: IllegalArgumentException) {
                    folderDao.delete(it)
                    null
                }
            }
        }

    override suspend fun add(folder: Folder) {
        folderDao.insert(folder.toDataEntity())
    }
    override suspend fun add(folders: List<Folder>) {
        folderDao.insert(folders.map { it.toDataEntity() })
    }

    override suspend fun remove(folder: Folder) {
        folderDao.delete(folder.toDataEntity())
    }
    override suspend fun remove(folders: List<Folder>) {
        folderDao.delete(folders.map { it.toDataEntity() })
    }

    override suspend fun update(folder: Folder) {
        folderDao.update(folder.toDataEntity())
    }
    override suspend fun update(folders: List<Folder>) {
        folderDao.update(folders.map { it.toDataEntity() })
    }
}
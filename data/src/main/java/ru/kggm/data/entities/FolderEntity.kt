package ru.kggm.data.entities
import androidx.room.*
import ru.kggm.domain.entities.Folder
import java.net.URI

@Entity(tableName = FolderEntity.TABLE)
data class FolderEntity(
    @PrimaryKey
    @ColumnInfo(name = URI)
    val uri: URI,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = FILE_COUNT)
    val fileCount: Int,) {

    companion object {
        const val TABLE = "folder_table"
        const val URI = "uri"
        const val NAME = "name"
        const val FILE_COUNT = "file_count"

        fun Folder.toDataEntity() = FolderEntity(
            uri = uri,
            name = name,
            fileCount = fileCount
        )
        fun FolderEntity.toDomainEntity() = Folder(
            uri = uri,
            name = name,
            fileCount = fileCount
        )
    }
}
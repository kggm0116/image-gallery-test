package ru.kggm.data.entities

import androidx.room.*
import ru.kggm.domain.entities.File
import java.net.URI

@Entity(
    tableName = FileEntity.TABLE,
//    foreignKeys = [
//        ForeignKey(
//            entity = FolderEntity::class,
//            parentColumns = arrayOf(FolderEntity.URI),
//            childColumns = arrayOf(FileEntity.FOLDER_URI),
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class FileEntity(
    @PrimaryKey @ColumnInfo(name = URI) val uri: URI,
    @ColumnInfo(name = FOLDER_URI) val folderUri: URI,
    @ColumnInfo(name = EXTENSION) val extension: String,
    @ColumnInfo(name = NAME) val name: String
){
    companion object {
        const val TABLE = "file_table"
        const val URI = "uri"
        const val FOLDER_URI = "folder"
        const val NAME = "name"
        const val EXTENSION = "extension"

        fun File.toDataEntity() = FileEntity(
            uri = uri,
            folderUri = folderUri,
            extension = extension,
            name = name
        )
        fun FileEntity.toDomainEntity() = File(
            uri = uri,
            folderUri = folderUri,
            extension = extension,
            name = name
        )
    }
}
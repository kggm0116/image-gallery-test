package ru.kggm.imagegallerytest.data.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = File.TABLE)
data class File(
    @PrimaryKey @ColumnInfo(name = URI) val uri: Uri,
    @ColumnInfo(name = NAME) val name: String?,
    @ColumnInfo(name = FOLDER_URI) val folderUri: Uri
){
    companion object {
        const val TABLE = "file_table"
        const val URI = "uri"
        const val NAME = "name"
        const val FOLDER_URI = "folder"
    }
}
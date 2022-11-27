package ru.kggm.imagegallerytest.data.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Folder.TABLE)
data class Folder(@PrimaryKey
                  @ColumnInfo(name = URI)
                  val uri: Uri,
                  @ColumnInfo(name = NAME)
                  val name: String?,
                  @ColumnInfo(name = FILE_COUNT, defaultValue = "0")
                  val fileCount: Int) {

    companion object {
        const val TABLE = "folder_table"
        const val URI = "uri"
        const val NAME = "name"
        const val FILE_COUNT = "file_count"
    }
}
package ru.kggm.data.database

import android.content.Context
import androidx.room.*
import ru.kggm.data.daos.FileDao
import ru.kggm.data.daos.FolderDao
import ru.kggm.data.entities.FileEntity
import ru.kggm.data.entities.FolderEntity

// https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0

@Database(
    entities = [FolderEntity::class, FileEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ImageGalleryDatabase : RoomDatabase() {
    abstract fun folderDao() : FolderDao
    abstract fun fileDao() : FileDao
    companion object {
        private const val NAME = "image_gallery_database"

        @Volatile
        private var INSTANCE: ImageGalleryDatabase? = null
        fun getDatabase(context: Context)
        : ImageGalleryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageGalleryDatabase::class.java,
                    NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
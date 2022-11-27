package ru.kggm.imagegallerytest.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import ru.kggm.imagegallerytest.data.database.daos.FileDao
import ru.kggm.imagegallerytest.data.database.daos.FolderDao
import ru.kggm.imagegallerytest.data.entities.File
import ru.kggm.imagegallerytest.data.entities.Folder

// https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0

@Database(
    entities = [Folder::class, File::class],
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
        fun getDatabase(
            context: Context,
            scope: CoroutineScope)
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
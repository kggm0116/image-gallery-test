package ru.kggm.imagegallerytest.data.database.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

// https://habr.com/ru/post/560272/

class UserPreferencesSerializer {

    companion object DefaultValues {
        const val DEFAULT_VALUE_FOLDERS_GRID_COLUMNS_LANDSCAPE = 3
        const val DEFAULT_VALUE_FOLDERS_GRID_COLUMNS_PORTRAIT = 3
        const val DEFAULT_VALUE_FILES_GRID_COLUMNS_LANDSCAPE = 3
        const val DEFAULT_VALUE_FILES_GRID_COLUMNS_PORTRAIT = 3
    }

    object SettingsSerializer : Serializer<ru.kggm.imagegallerytest.UserPreferences> {
        override val defaultValue: ru.kggm.imagegallerytest.UserPreferences = ru.kggm.imagegallerytest.UserPreferences.getDefaultInstance()
            .toBuilder()
            .setFoldersGridColumnsLandscape(DEFAULT_VALUE_FOLDERS_GRID_COLUMNS_LANDSCAPE)
            .setFoldersGridColumnsPortrait(DEFAULT_VALUE_FOLDERS_GRID_COLUMNS_PORTRAIT)
            .setFilesGridColumnsLandscape(DEFAULT_VALUE_FILES_GRID_COLUMNS_LANDSCAPE)
            .setFilesGridColumnsPortrait(DEFAULT_VALUE_FILES_GRID_COLUMNS_PORTRAIT)
            .build()

        override suspend fun readFrom(input: InputStream): ru.kggm.imagegallerytest.UserPreferences {
            try {
                return ru.kggm.imagegallerytest.UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(
            t: ru.kggm.imagegallerytest.UserPreferences,
            output: OutputStream
        ) = t.writeTo(output)
    }
}
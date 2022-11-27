package com.example.imagegallerytest.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.imagegallerytest.UserPreferences
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

    object SettingsSerializer : Serializer<UserPreferences> {
        override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
            .toBuilder()
            .setFoldersGridColumnsLandscape(DEFAULT_VALUE_FOLDERS_GRID_COLUMNS_LANDSCAPE)
            .setFoldersGridColumnsPortrait(DEFAULT_VALUE_FOLDERS_GRID_COLUMNS_PORTRAIT)
            .setFilesGridColumnsLandscape(DEFAULT_VALUE_FILES_GRID_COLUMNS_LANDSCAPE)
            .setFilesGridColumnsPortrait(DEFAULT_VALUE_FILES_GRID_COLUMNS_PORTRAIT)
            .build()

        override suspend fun readFrom(input: InputStream): UserPreferences {
            try {
                return UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(
            t: UserPreferences,
            output: OutputStream
        ) = t.writeTo(output)
    }
}
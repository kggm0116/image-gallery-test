package ru.kggm.data.datastore

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ru.kggm.data.UserPreferencesData
import java.io.InputStream
import java.io.OutputStream

// Source: https://habr.com/ru/post/560272/


@Suppress("BlockingMethodInNonBlockingContext")
object UserPreferencesDataSerializer : Serializer<UserPreferencesData> {
    override val defaultValue: UserPreferencesData = UserPreferencesData
        .getDefaultInstance().toBuilder()
        .setGridSizeFilesPortrait(3)
        .setGridSizeFilesLandscape(5)
        .setGridSizeFoldersPortrait(4)
        .setGridSizeFoldersLandscape(6)
        .addAllSupportedFileExtensions(listOf(".png", ".jpg", ".jpeg"))
        .build()

    override suspend fun readFrom(input: InputStream): UserPreferencesData {
        return try {
            UserPreferencesData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPreferencesData, output: OutputStream) = t.writeTo(output)
}
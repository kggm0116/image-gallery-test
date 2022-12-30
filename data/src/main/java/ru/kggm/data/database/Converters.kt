package ru.kggm.data.database

import androidx.room.TypeConverter
import java.net.URI

class Converters {
    @TypeConverter
    fun uriToString(value: URI): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToUri(value: String): URI {
        return URI.create(value)
    }
}
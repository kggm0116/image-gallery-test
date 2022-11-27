package com.example.imagegallerytest.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FolderWithFiles(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = Folder.URI,
        entityColumn = File.FOLDER_URI,
        entity = File::class,
    )
    val files: List<File>
)
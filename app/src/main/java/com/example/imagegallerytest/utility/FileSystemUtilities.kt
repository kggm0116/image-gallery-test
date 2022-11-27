package com.example.imagegallerytest.utility

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.example.imagegallerytest.data.entities.File
import com.example.imagegallerytest.data.entities.Folder


class FileSystemUtilities {
    companion object {
        fun Uri.toFolder(context: Context): Folder {
            return DocumentFile.fromTreeUri(context, this)!!.run {
                Folder(uri, name, 0)
            }
        }

        fun Folder.exists(context: Context): Boolean {
            return DocumentFile.fromTreeUri(context, uri)!!.exists()
        }

        fun Folder.getChildren(context: Context, nameFilter: Regex? = null): List<File> {
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                this.uri,
                DocumentsContract.getDocumentId(this.uri)
            )
            context.contentResolver.query(
                childrenUri,
                arrayOf(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME
                ), null, null, null
            )?.use {
                val results = mutableListOf<File>()
                while (it.moveToNext()) {
                    if (nameFilter == null || !nameFilter.matches(it.getString(1)))
                        continue
                    val uri = DocumentsContract.buildDocumentUriUsingTree(
                        this.uri,
                        it.getString(0)
                    )
                    results.add(File(uri, it.getString(1), this.uri))
                }
                return results
            }
            return emptyList()
        }
    }
}
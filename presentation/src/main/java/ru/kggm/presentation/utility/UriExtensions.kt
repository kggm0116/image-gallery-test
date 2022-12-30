package ru.kggm.presentation.utility

import android.content.ContentResolver
import android.net.Uri
import android.provider.DocumentsContract

class UriExtensions {
    companion object {
        fun Uri.getName() =
            this.path
                ?.substringAfterLast('/')
                ?.substringAfterLast(':')
                ?: ""

        fun Uri.getChildren(contentResolver: ContentResolver): List<Uri> {
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                this,
                DocumentsContract.getDocumentId(this)
            )
            contentResolver.query(
                childrenUri,
                arrayOf(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID
                ), null, null, null
            )?.use {
                val results = mutableListOf<Uri>()
                while (it.moveToNext()) {
                    val uri = DocumentsContract.buildDocumentUriUsingTree(
                        this,
                        it.getString(0)
                    )
                    results.add(uri)
                }
                return results
            }
            return emptyList()
        }
    }
}
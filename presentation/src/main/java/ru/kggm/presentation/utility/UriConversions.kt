package ru.kggm.presentation.utility

import android.net.Uri
import java.net.URI

class UriConversions {
    companion object {
        fun URI.toUri(): Uri = Uri.parse(this.toString())
        fun Uri.toURI(): URI = URI.create(this.toString())
    }
}
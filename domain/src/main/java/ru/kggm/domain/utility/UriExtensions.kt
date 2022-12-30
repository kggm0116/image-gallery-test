package ru.kggm.domain.utility

import java.net.URI

class UriExtensions {

    companion object {
        fun URI.getExtension() = '.' + this.path
            .substringAfterLast('/')
            .substringAfterLast('.', "")

        fun URI.getName() =
            this.path
                .substringAfterLast(':')
                .substringAfterLast('/')
    }

}
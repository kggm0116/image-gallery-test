package ru.kggm.domain.utility

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take

class FlowExtensions {
    companion object {
        suspend fun <T> Flow<T>.takeLast() = this.take(1).single()
    }
}
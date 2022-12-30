package ru.kggm.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kggm.domain.entities.*

interface UserPreferencesRepository {
    fun getPreferences(): Flow<UserPreferences>
    suspend fun setPreferences(preference: UserPreferences)
}
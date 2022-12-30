package ru.kggm.domain.usecases.preferences

import ru.kggm.domain.repositories.*
import javax.inject.Inject

class GetUserPreferences @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke() = userPreferencesRepository.getPreferences()
}
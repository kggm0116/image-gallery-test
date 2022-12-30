package ru.kggm.domain.usecases.files

import ru.kggm.domain.repositories.FileRepository
import javax.inject.Inject

class GetFiles @Inject constructor (
    private val fileRepository: FileRepository,
) {
    operator fun invoke() = fileRepository.get()
}
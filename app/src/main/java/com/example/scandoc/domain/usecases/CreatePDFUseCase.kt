package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.repositories.ImagesRepository
import java.util.UUID
import javax.inject.Inject

class CreatePDFUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
) {
    suspend fun execute(uuid: UUID) {
        imagesRepository.createPDF(uuid)
    }
}

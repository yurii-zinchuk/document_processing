package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.repositories.ImagesRepository
import com.example.scandoc.domain.repositories.PDFRepository
import java.util.UUID
import javax.inject.Inject

class CreatePDFUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val pdfRepository: PDFRepository,
) {
    suspend fun execute(uuid: UUID) {
        val imageFiles = imagesRepository.getImages(uuid)
        pdfRepository.createPDF(uuid, imageFiles)
    }
}

package com.example.scandoc.domain.usecases

import android.net.Uri
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import com.example.scandoc.domain.repositories.ImagesRepository
import com.example.scandoc.domain.repositories.PDFRepository
import java.util.UUID
import javax.inject.Inject

class CreateDocumentSetUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
        private val documentSetsRepository: DocumentSetsRepository,
        private val pdfRepository: PDFRepository,
    ) {
        suspend fun execute(
            uris: List<Uri>,
            name: String,
        ) {
            val uuid = UUID.randomUUID()
            imagesRepository.saveImages(uris, uuid)
                .let { success -> if (!success) return@execute }
            documentSetsRepository.saveDocumentSet(
                DocumentSet(
                    uuid = uuid,
                    name = name,
                    createdAt = System.currentTimeMillis(),
                ),
            )
            val imageFiles = imagesRepository.getImages(uuid)
            pdfRepository.createPDF(uuid, imageFiles)
        }
    }

package com.example.scandoc.domain.usecases

import com.example.scandoc.data.repositories.ImagesRepositoryImpl
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import java.util.UUID
import javax.inject.Inject

class DeleteDocumentSetUseCase @Inject constructor(
    private val documentSetsRepository: DocumentSetsRepository,
    private val imagesRepositoryImpl: ImagesRepositoryImpl,
) {
    suspend fun execute(uuid: UUID) {
        imagesRepositoryImpl.deleteImages(uuid)
        documentSetsRepository.deleteDocumentSet(uuid)
    }
}

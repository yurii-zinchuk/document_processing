package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.models.ProcessingStatus
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import java.util.UUID
import javax.inject.Inject

class UpdateProcessingStatusUseCase @Inject constructor(
    private val documentSetsRepository: DocumentSetsRepository,
) {
    suspend fun execute(uuid: UUID, newStatus: ProcessingStatus) {
        documentSetsRepository.updateDocumentSetProcessingStatus(uuid, newStatus)
    }
}

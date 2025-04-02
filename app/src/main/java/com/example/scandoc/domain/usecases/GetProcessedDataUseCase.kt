package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.models.ProcessedData
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import java.util.UUID
import javax.inject.Inject

class GetProcessedDataUseCase @Inject constructor(
    private val documentSetsRepository: DocumentSetsRepository,
) {
    fun execute(uuid: UUID): ProcessedData {
        return documentSetsRepository.getProcessedData(uuid)
    }

}

package com.zinchuk.scandoc.domain.usecases

import com.zinchuk.scandoc.domain.models.ProcessedData
import com.zinchuk.scandoc.domain.repositories.DocumentSetsRepository
import java.util.UUID
import javax.inject.Inject

class GetProcessedDataUseCase
    @Inject
    constructor(
        private val documentSetsRepository: DocumentSetsRepository,
    ) {
        fun execute(uuid: UUID): ProcessedData {
            return documentSetsRepository.getProcessedData(uuid)
        }
    }

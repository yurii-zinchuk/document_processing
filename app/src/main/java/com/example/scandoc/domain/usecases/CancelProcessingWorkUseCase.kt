package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.repositories.DocumentSetsRepository
import java.util.UUID
import javax.inject.Inject

class CancelProcessingWorkUseCase
    @Inject
    constructor(
        private val documentSetsRepository: DocumentSetsRepository,
    ) {
        fun execute(uuid: UUID) {
            documentSetsRepository.cancelProcessingWork(uuid)
        }
    }

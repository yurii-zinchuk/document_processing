package com.zinchuk.scandoc.domain.usecases

import com.zinchuk.scandoc.domain.repositories.DocumentSetsRepository
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

package com.zinchuk.scandoc.domain.usecases

import com.zinchuk.scandoc.domain.repositories.DocumentSetsRepository
import java.util.UUID
import javax.inject.Inject

class GetDocumentSetByUUIDUseCase
    @Inject
    constructor(
        private val documentSetsRepository: DocumentSetsRepository,
    ) {
        suspend fun execute(uuid: UUID) = documentSetsRepository.getDocumentSet(uuid)
    }

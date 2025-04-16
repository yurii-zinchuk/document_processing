package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.repositories.DocumentSetsRepository
import com.example.scandoc.domain.repositories.ZIPRepository
import java.util.UUID
import javax.inject.Inject

class ProcessDocumentSetUseCase
    @Inject
    constructor(
        private val documentSetsRepository: DocumentSetsRepository,
        private val zipRepository: ZIPRepository,
    ) {
        suspend fun execute(uuid: UUID): UUID {
            val zipFile = zipRepository.getZIP(uuid)
            val processingWorkId = documentSetsRepository.processDocumentSet(uuid, zipFile)
            return processingWorkId
        }
    }

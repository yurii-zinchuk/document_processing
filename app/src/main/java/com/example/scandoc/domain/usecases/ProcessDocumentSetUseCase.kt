package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.models.ProcessedData
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import com.example.scandoc.domain.repositories.PDFRepository
import java.util.UUID
import javax.inject.Inject

class ProcessDocumentSetUseCase @Inject constructor(
    private val documentSetsRepository: DocumentSetsRepository,
    private val pdfRepository: PDFRepository
) {

    suspend fun execute(uuid: UUID): UUID {
        val pdfFile = pdfRepository.getPDF(uuid)
        val processingWorkId = documentSetsRepository.processDocumentSet(uuid, pdfFile)
        return processingWorkId
    }

}

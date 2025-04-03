package com.example.scandoc.domain.usecases

import androidx.work.WorkInfo
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class GetDocumentSetWorkInfoUseCase
    @Inject
    constructor(
        private val documentSetsRepository: DocumentSetsRepository,
    ) {
        fun execute(uuid: UUID): Flow<List<WorkInfo>> {
            return documentSetsRepository.getDocumentSetWorkInfo(uuid)
        }
    }

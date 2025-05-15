package com.zinchuk.scandoc.domain.usecases

import androidx.paging.PagingData
import com.zinchuk.scandoc.domain.models.DocumentSet
import com.zinchuk.scandoc.domain.repositories.DocumentSetsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDocumentSetsUseCase
    @Inject
    constructor(
        private val documentSetsRepository: DocumentSetsRepository,
    ) {
        fun execute(): Flow<PagingData<DocumentSet>> {
            return documentSetsRepository.getAllDocumentSets()
        }
    }

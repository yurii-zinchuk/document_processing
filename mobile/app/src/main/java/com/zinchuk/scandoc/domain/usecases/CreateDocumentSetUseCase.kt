package com.zinchuk.scandoc.domain.usecases

import android.net.Uri
import com.zinchuk.scandoc.domain.models.DocumentSet
import com.zinchuk.scandoc.domain.repositories.DocumentSetsRepository
import com.zinchuk.scandoc.domain.repositories.ImagesRepository
import com.zinchuk.scandoc.domain.repositories.ZIPRepository
import java.util.UUID
import javax.inject.Inject

class CreateDocumentSetUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
        private val documentSetsRepository: DocumentSetsRepository,
        private val ZIPRepository: ZIPRepository,
    ) {
        suspend fun execute(
            uris: List<Uri>,
            name: String,
        ) {
            val uuid = UUID.randomUUID()
            imagesRepository.saveImages(uris, uuid)
                .let { success -> if (!success) return@execute }
            documentSetsRepository.saveDocumentSet(
                DocumentSet(
                    uuid = uuid,
                    name = name,
                    createdAt = System.currentTimeMillis(),
                ),
            )
            val imageFiles = imagesRepository.getImages(uuid)
            ZIPRepository.createZIP(uuid, imageFiles)
        }
    }

package com.zinchuk.scandoc.domain.usecases

import com.zinchuk.scandoc.domain.repositories.ImagesRepository
import java.io.File
import java.util.UUID
import javax.inject.Inject

class GetDocumentSetPreviewImageUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
    ) {
        suspend fun execute(uuid: UUID): File {
            return imagesRepository.getImages(uuid)
                .firstOrNull()
                ?: error("No preview found!")
        }
    }

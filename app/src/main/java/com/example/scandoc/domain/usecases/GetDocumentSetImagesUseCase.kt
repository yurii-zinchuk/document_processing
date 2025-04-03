package com.example.scandoc.domain.usecases

import com.example.scandoc.domain.repositories.ImagesRepository
import java.io.File
import java.util.UUID
import javax.inject.Inject

class GetDocumentSetImagesUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
    ) {
        suspend fun execute(uuid: UUID): List<File> {
            return imagesRepository.getImages(uuid)
        }
    }

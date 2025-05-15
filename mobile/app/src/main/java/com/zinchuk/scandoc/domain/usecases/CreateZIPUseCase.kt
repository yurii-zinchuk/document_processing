package com.zinchuk.scandoc.domain.usecases

import com.zinchuk.scandoc.domain.repositories.ImagesRepository
import com.zinchuk.scandoc.domain.repositories.ZIPRepository
import java.util.UUID
import javax.inject.Inject

class CreateZIPUseCase
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
        private val ZIPRepository: ZIPRepository,
    ) {
        suspend fun execute(uuid: UUID) {
            val imageFiles = imagesRepository.getImages(uuid)
            ZIPRepository.createZIP(uuid, imageFiles)
        }
    }

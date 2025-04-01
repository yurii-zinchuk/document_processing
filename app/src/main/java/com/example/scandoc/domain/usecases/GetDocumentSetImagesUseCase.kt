package com.example.scandoc.domain.usecases

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.paging.PagingData
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import com.example.scandoc.domain.repositories.ImagesRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID
import javax.inject.Inject

class GetDocumentSetImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
) {
    suspend fun execute(uuid: UUID): List<File> {
        return imagesRepository.getImages(uuid)
    }

}

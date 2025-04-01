package com.example.scandoc.presentation.mappers

import com.example.scandoc.domain.mappers.SuspendDataMapper
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.usecases.GetDocumentSetImagesUseCase
import com.example.scandoc.domain.usecases.GetDocumentSetPreviewImageUseCase
import com.example.scandoc.presentation.models.DocumentSetItemData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MapperDocumentSetDomainToUI @Inject constructor(
    private val getDocumentSetPreviewImageUseCase: GetDocumentSetPreviewImageUseCase,
    private val getDocumentSetImagesUseCase: GetDocumentSetImagesUseCase,
) : SuspendDataMapper<DocumentSet, DocumentSetItemData> {
    override suspend fun map(data: DocumentSet): DocumentSetItemData {
        val preview = getDocumentSetPreviewImageUseCase.execute(data.uuid)
        val numberOfPages = getDocumentSetImagesUseCase.execute(data.uuid).size
        return DocumentSetItemData(
            uuid = data.uuid,
            name = data.name,
            previewImage = preview,
            numberOfPages = numberOfPages,
            createdAt = SimpleDateFormat(
                DATE_PATTERN,
                Locale.getDefault()
            ).format(Date(data.createdAt))
        )
    }

    private companion object {
        private const val DATE_PATTERN = "MMM dd, yyyy 'at' HH:mm"
    }
}

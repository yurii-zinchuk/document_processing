package com.example.scandoc.presentation.models

import com.example.scandoc.domain.models.ProcessingStatus
import java.io.File
import java.util.UUID

data class DocumentSetItemData(
    val uuid: UUID,
    val name: String,
    val previewImage: File?,
    val numberOfPages: Int,
    val createdAt: String,
    val processingStatus: ProcessingStatus,
)

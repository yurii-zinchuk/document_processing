package com.zinchuk.scandoc.presentation.models

import java.io.File
import java.util.UUID

data class DocumentSetItemData(
    val uuid: UUID,
    val name: String,
    val previewImage: File?,
    val numberOfPages: Int,
    val createdAt: String,
)

package com.example.scandoc.domain.models

import androidx.collection.LongIntMap
import java.util.UUID

data class DocumentSet(
    val uuid: UUID,
    val name: String,
    val createdAt: Long,
)

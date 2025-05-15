package com.zinchuk.scandoc.domain.models

import java.util.UUID

data class DocumentSet(
    val uuid: UUID,
    val name: String,
    val createdAt: Long,
)

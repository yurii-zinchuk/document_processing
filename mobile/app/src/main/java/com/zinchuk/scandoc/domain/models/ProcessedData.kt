package com.zinchuk.scandoc.domain.models

data class ProcessedData(
    val text: String?,
    val entities: Map<String, List<String>>?,
)

package com.zinchuk.scandoc.domain.repositories

import java.io.File
import java.util.UUID

interface ZIPRepository {
    suspend fun createZIP(
        uuid: UUID,
        imageFiles: List<File>,
    )

    suspend fun getZIP(uuid: UUID): File
}

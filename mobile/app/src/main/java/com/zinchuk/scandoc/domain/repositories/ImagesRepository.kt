package com.zinchuk.scandoc.domain.repositories

import android.net.Uri
import java.io.File
import java.util.UUID

interface ImagesRepository {
    suspend fun saveImages(
        sourceURIs: List<Uri>,
        uuid: UUID,
    ): Boolean

    suspend fun getImages(uuid: UUID): List<File>

    suspend fun deleteImages(uuid: UUID)
}

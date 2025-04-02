package com.example.scandoc.domain.repositories

import java.io.File
import java.util.UUID

interface PDFRepository {

    suspend fun createPDF(uuid: UUID, imageFiles: List<File>)

    suspend fun getPDF(uuid: UUID): File

}

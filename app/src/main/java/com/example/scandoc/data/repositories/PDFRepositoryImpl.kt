package com.example.scandoc.data.repositories

import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import com.example.scandoc.data.storage.InternalStorage
import com.example.scandoc.domain.repositories.PDFRepository
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class PDFRepositoryImpl
    @Inject
    constructor(
        private val internalStorage: InternalStorage,
    ) : PDFRepository {
        override suspend fun createPDF(
            uuid: UUID,
            imageFiles: List<File>,
        ) {
            val pdfFile = PdfDocument()

            imageFiles.forEachIndexed { idx, image ->
                val bitmap = BitmapFactory.decodeFile(image.absolutePath)
                val pageInfo =
                    PdfDocument
                        .PageInfo
                        .Builder(
                            bitmap.width,
                            bitmap.height,
                            idx.inc(),
                        ).create()

                val page = pdfFile.startPage(pageInfo)
                val canvas = page.canvas

                canvas.drawBitmap(bitmap, 0f, 0f, null)
                pdfFile.finishPage(page)

                val destinationDirectory = getInternalPDFDirectory(uuid)
                if (!destinationDirectory.exists()) destinationDirectory.mkdirs()

                val pdfDestinationFile =
                    File(
                        destinationDirectory,
                        PDF_FILE_NAME,
                    )

                FileOutputStream(pdfDestinationFile).use { out ->
                    pdfFile.writeTo(out)
                }
            }
            pdfFile.close()
        }

        override suspend fun getPDF(uuid: UUID): File {
            return File(
                getInternalPDFDirectory(uuid),
                PDF_FILE_NAME,
            )
        }

        private fun getInternalPDFDirectory(uuid: UUID): File = File(internalStorage.getDirectory(uuid), PDF_DIRECTORY)

        private companion object {
            private const val PDF_FILE_NAME = "data.pdf"
            private const val PDF_DIRECTORY = "pdf"
        }
    }
